package prj.net.packet.system;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

import java.net.InetSocketAddress;

public class LoginPacket extends Packet {
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
        expectedReturnPackets.add(PacketType.worldState);
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }
}
