// A Java program for a Server 

import genericstuff.GenericServer;
import genericstuff.Packet;

import java.io.IOException;
import java.util.HashMap;

public class Server extends GenericServer
{
	private HashMap<ClientProxy, String> nameList = new HashMap<>();

	@Override
	protected void handleMessageFromClient(ClientProxy messageClient, Packet packet) {
		if (packet instanceof HelloPacket) {
			HelloPacket hello = (HelloPacket) packet;
			String name = hello.getName();
			nameList.put(messageClient, name);
			broadcastFromClient(messageClient, new HelloPacket(name));
		} else if (packet instanceof RosterPacket) {
			try {
				messageClient.sendMessageToClient(new RosterPacket(nameList.values()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (packet instanceof MessagePacket) {
			broadcastFromClient(messageClient, packet);
		} else if (packet instanceof GoodbyePacket) {
			removeClient(messageClient);
		}
	}

	protected void removeClient(ClientProxy client) {
		super.removeClient(client);
		String name = nameList.get(client);
		nameList.remove(client);
		broadcastFromClient(client, new GoodbyePacket(name));
	}

	protected void broadcastFromClient(ClientProxy messageClient, Packet message) {
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

	public static void main(String args[]) {
		try {
			Server test = new Server();
			test.start(55556);
		} catch (IOException i) {
			System.out.println(i);
		}
	}
} 