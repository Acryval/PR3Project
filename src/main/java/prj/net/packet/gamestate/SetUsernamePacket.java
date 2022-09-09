package prj.net.packet.gamestate;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class SetUsernamePacket extends Packet {
    private final String username;
    public SetUsernamePacket(String username) {
        super(PacketType.setUsername);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
