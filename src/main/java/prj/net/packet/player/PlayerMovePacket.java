package prj.net.packet.player;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class PlayerMovePacket extends Packet {
    @Override
    public PacketType getPacketType() {
        return PacketType.playerMove;
    }

    public enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private final Direction direction;
    private final boolean value;

    public PlayerMovePacket(Direction direction, boolean value) {
        this.direction = direction;
        this.value = value;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean value() {
        return value;
    }
}
