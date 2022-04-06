package prj.net.packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public abstract class Packet implements Serializable {
    public abstract PacketType getPacketType();
    public abstract boolean expectsAnswer();

    public static <T extends Packet> void send(Socket session, T data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(session.getOutputStream());
        out.writeObject(data);
        out.flush();
        out.close();
    }

    public static Packet receive(Socket session) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(session.getInputStream());
        Object data = in.readObject();
        in.close();

        if(data instanceof Packet packet){
            return packet;
        }
        return null;
    }

    public String getPacketName(){
        return getPacketType().packetName;
    }
}
