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

	// constructor to put ip address and port
	public BotClient(String address, int port)
	{
		super(address, port);
		super.begin();
	}

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
		BotClient client = new BotClient("127.0.0.1", 55556);

	}
}