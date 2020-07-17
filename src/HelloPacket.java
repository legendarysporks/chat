import genericstuff.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HelloPacket implements Packet {
	private String name;

	public HelloPacket() {
	}

	public HelloPacket(String name) {
		this.name = name;
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(name);
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		name = in.readUTF();
	}

	public String getName() {
		return name;
	}
}
