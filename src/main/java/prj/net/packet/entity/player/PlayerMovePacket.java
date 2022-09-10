package prj.net.packet.entity.player;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.world.Direction;

public class PlayerMovePacket extends Packet {
    private final String username;
    private final Direction dir;
    private final boolean value;
    public PlayerMovePacket(String username, Direction dir, boolean value) {
        super(PacketType.playerMove);
        this.username = username;
        this.dir = dir;
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public Direction getDir() {
        return dir;
    }

    public boolean getValue() {
        return value;
    }
}
