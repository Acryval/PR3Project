package prj.net.packet.entity.player;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class PlayerPosPacket extends Packet {
    private final String username;
    private final int x;
    private final int y;
    public PlayerPosPacket(String username, int x, int y) {
        super(PacketType.playerPos);
        this.username = username;
        this.x = x;
        this.y = y;
    }

    public String getUsername() {
        return username;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
