package prj.net.packet.gamestate;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class PassDataPacket extends Packet {
    public PassDataPacket() {
        super(PacketType.passData);
    }
}
