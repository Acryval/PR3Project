package prj.net.packet.gamestate;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class StartServerPacket extends Packet {
    public StartServerPacket() {
        super(PacketType.startServer);
    }
}
