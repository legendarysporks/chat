// A Java program for a Server 

import org.omg.CORBA.COMM_FAILURE;

import java.io.IOException;
import java.util.HashMap;

public class Server extends GenericServer
{
	private HashMap<ClientProxy, String> nameList = new HashMap<>();

	@Override
	protected void handleMessageFromClient(ClientProxy messageClient, Command command, String message) {
		switch (command) {
			case HELLO:
				nameList.put(messageClient, message);
				broadcastFromClient(messageClient, message+" joined");
				break;
			case ROSTER:
				StringBuilder builder = new StringBuilder();
				for (ClientProxy client : getClients()) {
					builder.append(nameList.get(client));
					builder.append(" ");
				}
				try {
					messageClient.sendMessageToClient(Command.MESSAGE, builder.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case MESSAGE:
				broadcastFromClient(messageClient, message);
				break;
			default:
				break;
		}


	}

	protected void removeClient(ClientProxy client) {
		super.removeClient(client);
		String name = nameList.get(client);
		nameList.remove(client);
		broadcastFromClient(client, name + " left");
	}

	protected void broadcastFromClient(ClientProxy messageClient, String message) {
		for (ClientProxy client : getClients()) {
			if (client != messageClient) {
				try {
					client.sendMessageToClient(Command.MESSAGE, message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String args[])
	{
		try{
				Server test = new Server();
				test.start(55556);
		}
		catch(IOException i){
			System.out.println(i);
		}
	}
} 