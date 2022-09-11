package prj.world;

import prj.net.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class PreparedPackets {
    private final List<Packet> toSend;
    private final List<Packet> toBroadcast;

    public PreparedPackets() {
        toSend = new ArrayList<>();
        toBroadcast = new ArrayList<>();
    }

    public List<Packet> getToSend() {
        return toSend;
    }

    public List<Packet> getToBroadcast() {
        return toBroadcast;
    }

    public void addToSend(Packet p){
        toSend.add(p);
    }

    public void addToBroadcast(Packet p){
        toBroadcast.add(p);
    }
}
