package genericstuff;// A Java program for a Server

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GenericServer {
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
		for (ClientProxy client : clients) {
			client.interrupt();
		}
		joiner.interrupt();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ---- subclass interface

	protected abstract void handleMessageFromClient(ClientProxy client, Packet packet);

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
		private final PacketConnection connection;

		public ClientProxy(Socket socket) throws IOException {
			connection = new PacketConnection(socket);
		}

		@Override
		public void run() {
			// starts server and waits for a connection

			try {
				while (!isInterrupted()) {
					handleMessageFromClient(this, connection.read());
				}

			} catch (IOException i) {
				System.out.println(i);
				GenericServer.this.stop();
			}
			System.out.println("Closing connection");

			connection.close();
			removeClient(this);
		}

		public void sendMessageToClient(Packet packet) throws IOException {
			connection.write(packet);
		}
	}
}