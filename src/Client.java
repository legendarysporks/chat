// A Java program for a Client 
import java.net.*;
import java.io.*;

public class Client
{
	// initialize socket and keyboardInput output streams
	private Socket socket            = null;
	private DataInputStream keyboardInput = null;
	private DataInputStream socketInput = null;
	private DataOutputStream socketOutput = null;

	// constructor to put ip address and port
	public Client(String address, int port)
	{
		// establish a connection
		try
		{
			socket = new Socket(address, port);
			System.out.println("Connected");

			// takes keyboardInput from terminal
			keyboardInput = new DataInputStream(System.in);

			// sends output to the socket
			socketOutput = new DataOutputStream(socket.getOutputStream());
			socketInput = new DataInputStream(socket.getInputStream());
		}
		catch(UnknownHostException u)
		{
			System.out.println(u);
		}
		catch(IOException i)
		{
			System.out.println(i);
		}

		// keep reading until "Over" is keyboardInput
		Thread toServerThread = new Thread() {
			public void run() {
				// string to read message from keyboardInput
				String line = "";

				while (!line.equals("Over"))
				{
					try
					{
						line = keyboardInput.readUTF();
						socketOutput.writeUTF(line);
					}
					catch(IOException i)
					{
						System.out.println(i);
					}
				}
			}
		};

		Thread fromServerThread = new Thread() {
			public void run() {
				// string to read message from keyboardInput
				String line = "";

				while (!line.equals("Over"))
				{
					try
					{
						line = socketInput.readUTF();
						System.out.println(line);
					}
					catch(IOException i)
					{
						System.out.println(i);
					}
				}
			}
		};
		fromServerThread.start();
		toServerThread.start();


		// close the connection
		try
		{
			keyboardInput.close();
			socketOutput.close();
			socket.close();
		}
		catch(IOException i)
		{
			System.out.println(i);
		}
	}

	public static void main(String args[])
	{
		Client client = new Client("127.0.0.1", 55556);
	}
} 