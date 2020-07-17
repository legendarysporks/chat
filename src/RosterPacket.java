import genericstuff.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class RosterPacket implements Packet {
	private Collection<String> names;

	public RosterPacket() {
	}

	public RosterPacket(Collection<String> names) {
		this.names = names;
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(names.size());
		for (String name : names) {
			out.writeUTF(name);
		}
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		names = new ArrayList<>();
		int size = in.readInt();
		for (int i = 0; i < size; i++) {
			names.add(in.readUTF());
		}
	}

	public Collection<String> getNames() {
		return names;
	}
}
