package prj.net.packet;

import java.net.InetSocketAddress;

public class LogoutPacket extends Packet{
    @Override
    public PacketType getPacketType() {
        return PacketType.logout;
    }

    private final InetSocketAddress clientAddress;

    public LogoutPacket(InetSocketAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }
}
