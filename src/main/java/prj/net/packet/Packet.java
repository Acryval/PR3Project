package prj.net.packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet implements Serializable {
    public abstract PacketType getPacketType();
    public List<PacketType> expectedReturnPackets = new ArrayList<>();

    public static void send(Socket session, List<Packet> data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(session.getOutputStream());
        for(Packet p : data){
            out.writeObject(p);
        }
        out.flush();
        out.close();
    }

    public static void send(Socket session, Packet...data) throws IOException {
        Packet.send(session, List.of(data));
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
}
