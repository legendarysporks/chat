// A Java program for a Server 

import java.io.IOException;

public class Server extends GenericServer
{
	public Server(int port) throws IOException {
		super(port);
	}

	protected void handleMessageFromClient(ClientProxy messageClient, String message) {
		for (ClientProxy client : getClients()) {
			if (client != messageClient) {
				try {
					client.sendMessageToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String args[])
	{
		try{
				Server test = new Server(55556);
				test.start();
		}
		catch(IOException i){
			System.out.println(i);
		}
	}
} 