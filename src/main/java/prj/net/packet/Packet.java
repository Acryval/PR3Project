package prj.net.packet;

import prj.log.Logger;
import prj.net.packet.system.MultiPacket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Packet implements Serializable {
    protected PacketType type;
    protected List<PacketType> expectedReturnPackets;

    public Packet(PacketType type) {
        this.type = type;
        this.expectedReturnPackets = new ArrayList<>();
    }

    public PacketType getType() {
        return type;
    }

    public String getName(){
        return "" + type;
    }

    public List<PacketType> getExpectedReturnPackets() {
        return expectedReturnPackets;
    }

    public void expect(PacketType...packets){
        expectedReturnPackets.addAll(List.of(packets));
    }

    public static void send(Logger sender, Socket sessionSocket, List<Packet> dataIn) {
        if(sessionSocket == null || dataIn.size() == 0) return;

        try {
            ObjectOutputStream oos = new ObjectOutputStream(sessionSocket.getOutputStream());
            sender.announcePackets(sessionSocket.getInetAddress(), "sending", dataIn);

            if (dataIn.size() > 1) {
                Packet mp = new MultiPacket(dataIn.size());
                sender.dbg("multiple packets; sending additional packet: " + mp.getName());
                oos.writeObject(mp);
            }

            for (Packet p : dataIn) {
                oos.writeObject(p);
            }

            oos.flush();
        }catch(IOException e){
            sender.err("failed to send packets: " + e.getMessage());
        }
    }

    public static List<Packet> receive(Logger receiver, Socket sessionSocket) {
        List<Packet> inPackets = new ArrayList<>();

        if(sessionSocket == null){
            receiver.err("socket is null");
            return inPackets;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(sessionSocket.getInputStream());
            Object o = ois.readObject();

            if (o instanceof Packet packet) {
                receiver.announcePackets(null, "received", packet);

                if (packet instanceof MultiPacket mp) {
                    int pnum = mp.getNumberOfPackets();

                    receiver.dbg(pnum + " packets incoming");

                    for (int i = 0; i < mp.getNumberOfPackets(); i++) {
                        o = ois.readObject();
                        if (o instanceof Packet p) {
                            receiver.announcePackets(null, String.format("(%d/%d)", (i + 1), pnum), p);
                            inPackets.add(p);
                        } else {
                            receiver.announcePackets(null, String.format("(%d/%d)", (i + 1), pnum));
                        }
                    }
                } else {
                    inPackets.add(packet);
                }
            } else {
                receiver.warn("received corrupted packet");
            }
        } catch (IOException | ClassNotFoundException e) {
            receiver.err("receiving packets failed: " + e.getMessage());
        }

        return inPackets;
    }
}
