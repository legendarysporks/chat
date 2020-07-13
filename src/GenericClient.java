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

	public void start(String address, int port) {
		// establish a connection
		try
		{
			socket = new Socket(address, port);
			System.out.println("Connected");

			// sends output to the socket
			socketOutput = new DataOutputStream(socket.getOutputStream());
			socketInput = new DataInputStream(socket.getInputStream());
		}
		catch(IOException i)
		{
			System.out.println(i);
		}

		fromServerThread = new FromServerThread();
		fromServerThread.start();
	}

	public void stop() {
		fromServerThread.interrupt();
		try {
			socketInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socketOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ---- subclass interface

	protected void sendMessageToServer(Command command, String message) throws IOException {
		socketOutput.writeUTF(command.toString());
		socketOutput.writeUTF(message);
	}

	protected abstract void handleMessageFromServer(Command command, String message);

	// ----

	private class FromServerThread extends Thread {
		public void run() {
			while (!isInterrupted())
			{
				try
				{
					Command command = Command.valueOf(socketInput.readUTF());
					String line = socketInput.readUTF();
					handleMessageFromServer(command, line);
				}
				catch(IOException i)
				{
					System.out.println(i);
				}
			}
		}
	}
}