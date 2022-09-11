package prj.net.packet.gamestate;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

import java.net.InetSocketAddress;

public class ConnectToServerPacket extends Packet {
    private final InetSocketAddress address;

    public ConnectToServerPacket(InetSocketAddress address) {
        super(PacketType.connectToServer);
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }
}
