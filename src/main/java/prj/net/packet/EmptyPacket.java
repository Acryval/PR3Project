package prj.net.packet;

public class EmptyPacket extends Packet{
    @Override
    public PacketType getPacketType() {
        return PacketType.empty;
    }
}
