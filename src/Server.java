// A Java program for a Server 
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread
{
	//initialize socket and socketInput stream
	private ServerSocket serverSocket;
	private boolean running = true;
	private List<ClientListener> clients = new ArrayList<>();
	// constructor with port

	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	@Override
	public void run() {
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				ClientListener listener = new ClientListener(socket);
				clients.add(listener);
				listener.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void broadcast(String msg) {
		for (ClientListener client : clients) {
			try {
				client.socketOutput.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
				// can we remove this client since we can't communicate?
			}
		}
	}

	private class ClientListener extends Thread {
		private Socket socket;
		private DataOutputStream socketOutput;
		private DataInputStream socketInput;

		public ClientListener(Socket socket) throws IOException {
			this.socket = socket;
			socketOutput = new DataOutputStream(socket.getOutputStream());
			socketInput = new DataInputStream(socket.getInputStream());
		}

		@Override
		public void run() {
			// starts server and waits for a connection
			try {
				// takes socketInput from the client socket
				String line = "";

				// reads message from client until "Over" is sent
				while (!line.equals("Over")) {
					try {
						line = socketInput.readUTF();
						System.out.println(line);
						broadcast(line);

					} catch (IOException i) {
						System.out.println(i);
					}
				}
				running = false;
				System.out.println("Closing connection");

				// close connection
				socket.close();
				socketInput.close();
				socketOutput.close();
			} catch (IOException i) {
				System.out.println(i);
			}
		}
	}

	public static void main(String args[])
	{
		try{
			while(true){
				Server test = new Server(55556);
				test.start();
			}
		}
		catch(IOException i){
			System.out.println(i);
		}
	}
} 