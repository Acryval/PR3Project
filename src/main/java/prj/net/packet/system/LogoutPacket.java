package prj.net.packet.system;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

import java.net.InetSocketAddress;

public class LogoutPacket extends Packet {
    public LogoutPacket() {
        super(PacketType.logout);
    }
}
