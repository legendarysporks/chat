// A Java program for a Client 

import genericstuff.GenericClient;
import genericstuff.Packet;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;

public class Client extends GenericClient {
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
	protected void handleMessageFromServer(Packet command) {
		if (command instanceof MessagePacket) {
			System.out.println(((MessagePacket) command));
		} else if (command instanceof RosterPacket) {
			RosterPacket rosterPacket = (RosterPacket) command;
			Collection<String> names = rosterPacket.getNames();
			if (names.size() == 0) {
				System.out.println("You are the first to arrive");
			} else {
				for (String name : names) {
					System.out.println("\t" + name);
				}
			}
		}
	}

	private class ToServerThread extends Thread {
		public void run() {
			// string to read message from keyboardInput
			try {
				sendMessageToServer(new HelloPacket(name));
				String line = "";
				while (!line.equalsIgnoreCase("/over") && !interrupted()) {
					line = keyboardInput.readLine();
					if (line.equalsIgnoreCase("/list")) {
						sendMessageToServer(new RosterPacket());
					} else if (!line.equalsIgnoreCase("/over")) {
						sendMessageToServer(new MessagePacket(line));
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