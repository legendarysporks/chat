// A Java program for a Server 
import java.net.*;
import java.io.*;

public class Server extends Thread
{
	//initialize socket and input stream
	private Socket          socket   = null;
	private ServerSocket    server   = null;
	private DataInputStream in       =  null;
	static int connections = 0;
	// constructor with port

	public Server(Socket s){
		socket = s;
	}

	public void run()
	{
		// starts server and waits for a connection
		try
		{

			System.out.println("Server started");

			System.out.println("Waiting for a client ...");


			System.out.println("Client accepted");

			// takes input from the client socket
			in = new DataInputStream(
					new BufferedInputStream(socket.getInputStream()));

			String line = "";

			// reads message from client until "Over" is sent
			while (!line.equals("Over"))
			{
				try
				{
					line = in.readUTF();
					System.out.println(line);

				}
				catch(IOException i)
				{
					System.out.println(i);
				}
			}
			System.out.println("Closing connection");

			// close connection
			socket.close();
			connections--;
			System.out.println(connections);
			in.close();
		}
		catch(IOException i)
		{
			System.out.println(i);
		}
	}

	public static void main(String args[])
	{
		try{
			ServerSocket testSocket = new ServerSocket(5000);
			while(true){
				Server test = new Server(testSocket.accept());
				test.start();
			}
		}
		catch(IOException i){
			System.out.println(i);
		}


	}
} 