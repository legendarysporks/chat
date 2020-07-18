package genericstuff;// A Java program for a Client

import java.io.IOException;
import java.net.Socket;

public abstract class GenericClient {
	// initialize socket and keyboardInput output streams
	private PacketConnection connection;
	private Thread fromServerThread;

	public void start(String address, int port) {
		// establish a connection
		try {
			connection = new PacketConnection(new Socket(address, port));
		} catch (IOException i) {
			System.out.println(i);
		}

		fromServerThread = new FromServerThread();
		fromServerThread.start();
	}

	public void stop() {
		fromServerThread.interrupt();
		connection.close();
	}

	// ---- subclass interface

	protected void sendMessageToServer(Packet command) throws IOException {
		connection.write(command);
	}

	protected abstract void handleMessageFromServer(Packet command);

	// ----

	private class FromServerThread extends Thread {
		public void run() {
			while (!isInterrupted()) {
				try {
					handleMessageFromServer(connection.read());
				} catch (IOException i) {
					System.out.println(i);
					GenericClient.this.stop();
				}
			}
		}
	}
}