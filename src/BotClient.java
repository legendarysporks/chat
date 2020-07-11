// A Java program for a Client 
import java.io.IOException;
import java.util.Random;

public class BotClient extends GenericClient
{
	private static String[] messages = {
			"For sure!",
			"Like whoa.",
			"Huh?"
	};

	protected void handleMessageFromServer(String message)
	{
		try {
			sendMessageToServer(messages[new Random().nextInt(messages.length)]);
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