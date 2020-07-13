// A Java program for a Server 
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GenericServer
{
	//initialize socket and socketInput stream
	private ServerSocket serverSocket;
	private List<ClientProxy> clients = new ArrayList<>();
	private ClientJoiner joiner;
	// constructor with port

	public List<ClientProxy> getClients() {
		return Collections.unmodifiableList(clients);
	}

	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		joiner = new ClientJoiner();
		joiner.start();
	}

	public void stop() {
		joiner.interrupt();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ---- subclass interface

	protected abstract void handleMessageFromClient(ClientProxy client, Command command, String message);

	protected void addClient(Socket clientSocket) throws IOException {
		ClientProxy listener = new ClientProxy(clientSocket);
		clients.add(listener);
		listener.start();
	}

	protected void removeClient(ClientProxy client) {
		clients.remove(client);
		client.interrupt();
		if(clients.size() == 0){
			stop();
		}
	}

	// ----

	private class ClientJoiner extends Thread {
		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					addClient(serverSocket.accept());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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

				try {
					while (!isInterrupted()) {
						Command command = Command.valueOf(socketInput.readUTF());
						String line = socketInput.readUTF();
						handleMessageFromClient(this, command, line);
					}

				} catch (IOException i) {
					System.out.println(i);

				}
				System.out.println("Closing connection");

				// close connection
				socket.close();
				socketInput.close();
				socketOutput.close();
				removeClient(this);
			} catch (IOException i) {
				System.out.println(i);
			}
		}

		protected void sendMessageToClient(Command command, String message) throws IOException {
			socketOutput.writeUTF(command.toString());
			socketOutput.writeUTF(message);
		}
	}
}