package prj.net.packet;

public class MultiPacket extends Packet{
    @Override
    public PacketType getPacketType() {
        return PacketType.multiPacket;
    }

    private final int numberOfPackets;

    public MultiPacket(int numberOfPackets) {
        this.numberOfPackets = numberOfPackets;
    }

    public int getNumberOfPackets() {
        return numberOfPackets;
    }
}
