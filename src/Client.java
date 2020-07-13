// A Java program for a Client 
import java.net.*;
import java.io.*;

public class Client extends GenericClient
{
	// initialize socket and keyboardInput output streams
	private DataInputStream keyboardInput = null;
	private Thread toServerThread;
	private String name;

	public Client(String name) {
		this.name = name;
	}

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

	@Override
	protected void handleMessageFromServer(Command command, String message)
	{
		switch (command) {
			case MESSAGE:
				System.out.println(message);
				break;
			case ROSTER:
			case HELLO:
			default:
				break;
		}
	}

	private class ToServerThread extends Thread {
		public void run() {
			// string to read message from keyboardInput
			try {
				sendMessageToServer(Command.HELLO, name);
				String line = "";
				while (!line.equalsIgnoreCase("/over") && !interrupted()) {
					line = keyboardInput.readLine();
					if (line.equalsIgnoreCase("/list")) {
						sendMessageToServer(Command.ROSTER, "");
					} else if(!line.equalsIgnoreCase("/over")){
						sendMessageToServer(Command.MESSAGE, line);
					}
				}
			} catch (IOException i) {
				System.out.println(i);
			}
			Client.this.stop();
		}
	}

	public static void main(String args[])
	{
		Client client = new Client(args[0]);
		client.start("127.0.0.1", 55556);
	}
} 