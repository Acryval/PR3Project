package prj.net.packet.world;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.wall.Wall;

import java.awt.*;

public class BlockBrokenPacket extends Packet {
    private final Point pos;
    private final Wall wall;
    public BlockBrokenPacket(Point pos, Wall wall) {
        super(PacketType.blockBroken);
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
