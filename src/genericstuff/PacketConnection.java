package genericstuff;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class PacketConnection {
	private final Socket socket;
	private final DataInputStream socketInput;
	private final DataOutputStream socketOutput;

	public PacketConnection(Socket socket) throws IOException {
		this.socket = socket;
		socketOutput = new DataOutputStream(socket.getOutputStream());
		socketInput = new DataInputStream(socket.getInputStream());
	}

	public void close() {
		try {
			socketInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socketOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(Packet packet) throws IOException {
		socketOutput.writeUTF(packet.getClass().getName());
		packet.write(socketOutput);
	}

	public Packet read() throws IOException {
		String className = socketInput.readUTF();
		try {
			Class<?> clazz = Class.forName(className);
			Packet result = (Packet) clazz.getConstructor().newInstance();
			result.read(socketInput);
			return result;
		} catch (ClassNotFoundException | NoSuchMethodException |
				InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

}
