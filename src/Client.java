// A Java program for a Client 
import java.net.*;
import java.io.*;

public class Client extends GenericClient
{
	// initialize socket and keyboardInput output streams
	private DataInputStream keyboardInput = null;
	private Thread toServerThread;

	public void start(String address, int port) {
		super.start(address, port);
		// establish a connection
		// takes keyboardInput from terminal
		keyboardInput = new DataInputStream(System.in);


		// keep reading until "Over" is keyboardInput
		toServerThread = new ToServerThread();
		toServerThread.start();

		// close the connection
	}

	public void stop() {
		super.stop();
		toServerThread.interrupt();
	}

	protected void handleMessageFromServer(String message)
	{
		System.out.println(message);
	}

	private class ToServerThread extends Thread {
		public void run() {
			// string to read message from keyboardInput
			String line = "";
			try {
				while (!line.equals("Over") && !interrupted()) {
					line = keyboardInput.readLine();
					sendMessageToServer(line);
				}
			} catch (IOException i) {
				System.out.println(i);
			}
			Client.this.stop();
		}
	}

	public static void main(String args[])
	{
		Client client = new Client();
		client.start("127.0.0.1", 55556);
	}
} 