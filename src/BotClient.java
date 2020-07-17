// A Java program for a Client 
import genericstuff.GenericClient;
import genericstuff.Packet;

import java.io.IOException;
import java.util.Random;

public class BotClient extends GenericClient {
	private static String[] messages = {
			"For sure!",
			"Like whoa.",
			"Huh?"
	};
	private Random rand = new Random();

	@Override
	protected void handleMessageFromServer(Packet command) {
		try {
			sendMessageToServer(new MessagePacket(messages[rand.nextInt(messages.length)]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[])
	{
		BotClient client = new BotClient();
		client.start("127.0.0.1", 55556);
	}
}