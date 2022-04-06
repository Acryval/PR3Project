package prj.net.packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Packet{
    public static <T extends PacketDataType> void send(Socket session, T data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(session.getOutputStream());
        out.writeObject(data);
        out.flush();
        out.close();
    }

    public static Object receive(Socket session) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(session.getInputStream());
        Object data = in.readObject();
        in.close();
        return data;
    }
}
