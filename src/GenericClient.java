// A Java program for a Client 
import java.net.*;
import java.io.*;

public abstract class GenericClient
{
	// initialize socket and keyboardInput output streams
	private Socket socket            = null;
	private DataInputStream socketInput = null;
	private DataOutputStream socketOutput = null;
	private Thread fromServerThread;

	// constructor to put ip address and port
	public GenericClient(String address, int port)
	{
		// establish a connection
		try
		{
			socket = new Socket(address, port);
			System.out.println("Connected");

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

	}

	public void begin() {
		Thread fromServerThread = new Thread() {
			public void run() {
				while (!isInterrupted())
				{
					try
					{
						String line = socketInput.readUTF();
						handleMessageFromServer(line);
					}
					catch(IOException i)
					{
						System.out.println(i);
					}
				}
			}
		};
		fromServerThread.start();
	}

	public void end() {
		fromServerThread.interrupt();
	}

	protected void sendMessageToServer(String message) throws IOException {
		socketOutput.writeUTF(message);
	}

	protected abstract void handleMessageFromServer(String message);
}