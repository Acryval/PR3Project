package prj.net.packet.world;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.wall.Wall;

import java.awt.*;

public class BlockPlacedPacket extends Packet {
    private final Point pos;
    private final Wall oldWall, newWall;
    public BlockPlacedPacket(Point pos, Wall oldWall, Wall newWall) {
        super(PacketType.blockPlaced);
        this.pos = pos;
        this.oldWall = oldWall;
        this.newWall = newWall;
    }

    public Point getPos() {
        return pos;
    }

    public Wall getOldWall() {
        return oldWall;
    }

    public Wall getNewWall() {
        return newWall;
    }
}
