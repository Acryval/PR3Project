package prj.net.packet.entity.player;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class GetPlayerPosPacket extends Packet {
    private final String username;
    public GetPlayerPosPacket(String username) {
        super(PacketType.getPlayerPos);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
