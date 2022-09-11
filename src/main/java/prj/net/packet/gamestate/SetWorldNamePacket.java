package prj.net.packet.gamestate;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class SetWorldNamePacket extends Packet {
    private final String worldName;
    public SetWorldNamePacket(String name) {
        super(PacketType.setWorldName);
        worldName = name;
    }

    public String getWorldName() {
        return worldName;
    }
}
