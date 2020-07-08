// A Java program for a Server 
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GenericServer extends Thread
{
	//initialize socket and socketInput stream
	private ServerSocket serverSocket;
	private List<ClientProxy> clients = new ArrayList<>();
	// constructor with port

	public GenericServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				ClientProxy listener = new ClientProxy(socket);
				clients.add(listener);
				listener.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public List<ClientProxy> getClients() {
		return Collections.unmodifiableList(clients);
	}

	protected class ClientProxy extends Thread {
		private Socket socket;
		private DataOutputStream socketOutput;
		private DataInputStream socketInput;

		public ClientProxy(Socket socket) throws IOException {
			this.socket = socket;
			socketOutput = new DataOutputStream(socket.getOutputStream());
			socketInput = new DataInputStream(socket.getInputStream());
		}

		@Override
		public void run() {
			// starts server and waits for a connection
			try {
				while (!isInterrupted()) {
					try {
						handleMessageFromClient(this, socketInput.readUTF());

					} catch (IOException i) {
						System.out.println(i);
					}
				}
				System.out.println("Closing connection");

				// close connection
				socket.close();
				socketInput.close();
				socketOutput.close();
			} catch (IOException i) {
				System.out.println(i);
			}
		}

		protected void sendMessageToClient(String message) throws IOException {
			socketOutput.writeUTF(message);
		}
	}

	protected abstract void handleMessageFromClient(ClientProxy client, String message);
}