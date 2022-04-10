package prj.net.packet.system;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class MultiPacket extends Packet {
    @Override
    public PacketType getPacketType() {
        return PacketType.multiPacket;
    }

    @Override
    public String getPacketName() {
        return super.getPacketName() + String.format("(%d packets)", numberOfPackets);
    }

    private final int numberOfPackets;

    public MultiPacket(int numberOfPackets) {
        this.numberOfPackets = numberOfPackets;
    }

    public int getNumberOfPackets() {
        return numberOfPackets;
    }
}
