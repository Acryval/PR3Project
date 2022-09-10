package prj.net.packet.world;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.wall.Wall;

import java.awt.*;

public class BlockPlacedPacket extends Packet {
    private final Point pos;
    private final Wall wall;
    public BlockPlacedPacket(Point pos, Wall wall) {
        super(PacketType.blockPlaced);
        this.pos = pos;
        this.wall = wall;
    }

    public Point getPos() {
        return pos;
    }

    public Wall getWall() {
        return wall;
    }
}
