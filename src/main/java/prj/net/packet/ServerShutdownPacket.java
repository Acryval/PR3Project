package prj.net.packet;

public class ServerShutdownPacket extends Packet{
    @Override
    public PacketType getPacketType() {
        return PacketType.serverShutdown;
    }
}
