package prj.net.packet.system;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class ServerShutdownPacket extends Packet {
    public ServerShutdownPacket() {
        super(PacketType.serverShutdown);
    }
}
