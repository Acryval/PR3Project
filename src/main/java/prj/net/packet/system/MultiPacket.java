package prj.net.packet.system;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class MultiPacket extends Packet {
    private final int numberOfPackets;

    public MultiPacket(int numberOfPackets) {
        super(PacketType.multiPacket);
        this.numberOfPackets = numberOfPackets;
    }

    public int getNumberOfPackets() {
        return numberOfPackets;
    }

    @Override
    public String getName() {
        return super.getName() + "(n:" + numberOfPackets + ")";
    }
}
