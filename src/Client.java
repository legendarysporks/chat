// A Java program for a Client 
import java.net.*;
import java.io.*;

public class Client extends GenericClient
{
	// initialize socket and keyboardInput output streams
	private DataInputStream keyboardInput = null;

	// constructor to put ip address and port
	public Client(String address, int port)
	{
		super(address, port);
		// establish a connection
		// takes keyboardInput from terminal
		keyboardInput = new DataInputStream(System.in);


		// keep reading until "Over" is keyboardInput
		Thread toServerThread = new Thread() {
			public void run() {
				// string to read message from keyboardInput
				String line = "";

				while (!line.equals("Over"))
				{
					try
					{
						line = keyboardInput.readLine();
						sendMessageToServer(line);
					}
					catch(IOException i)
					{
						System.out.println(i);
					}
				}
			}
		};

		toServerThread.start();
		super.begin();


		// close the connection

	}

	protected void handleMessageFromServer(String message)
	{
		System.out.println(message);
	}

	public static void main(String args[])
	{

		Client client = new Client("127.0.0.1", 55556);

	}
} 