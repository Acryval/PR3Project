package prj.net.packet;

import java.net.InetSocketAddress;

public class LoginPacket extends Packet{
    @Override
    public PacketType getPacketType() {
        return PacketType.login;
    }

    @Override
    public String getPacketName() {
        return super.getPacketName() + String.format("(%s)", clientAddress);
    }

    private final InetSocketAddress clientAddress;

    public LoginPacket(InetSocketAddress clientAddress) {
        this.clientAddress = clientAddress;
        expectedReturnPackets.add(PacketType.getWorldState);
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }
}
