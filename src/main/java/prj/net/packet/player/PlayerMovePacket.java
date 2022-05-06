package prj.net.packet.player;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.world.Direction;

public class PlayerMovePacket extends Packet {
    private final Direction direction;
    private final boolean value;

    public PlayerMovePacket(Direction direction, boolean value) {
        super(PacketType.playerMove);
        this.direction = direction;
        this.value = value;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean value() {
        return value;
    }

    @Override
    public String getName() {
        return super.getName() + "(dir: " + direction.getName() + ", v:" + value + ")";
    }
}
