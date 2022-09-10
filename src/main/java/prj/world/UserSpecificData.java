package prj.world;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

import java.util.ArrayList;
import java.util.List;

public class UserSpecificData {
    private boolean logout;
    private final List<Packet> storedPackets;
    private final List<PacketType> expectedPackets;

    public UserSpecificData(boolean logout) {
        this.logout = logout;
        this.storedPackets = new ArrayList<>();
        this.expectedPackets = new ArrayList<>();
    }

    public UserSpecificData() {
        this(false);
    }

    public void setLogout(boolean logout) {
        this.logout = logout;
    }

    public boolean isLogout() {
        return logout;
    }

    public List<PacketType> getExpectedPackets() {
        return expectedPackets;
    }

    public List<Packet> getStoredPackets() {
        return storedPackets;
    }
    public void addExpectedPackets(List<PacketType> packets){
        expectedPackets.addAll(packets);
    }

    public void storePacket(Packet data){
        storedPackets.add(data);
    }
}
