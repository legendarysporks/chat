import genericstuff.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessagePacket implements Packet {
	private String message;

	public MessagePacket() {
	}

	public MessagePacket(String message) {
		this.message = message;
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(message);
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		message = in.readUTF();
	}

	public String toString() {
		return message;
	}
}
