package prj.net.packet.world;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class ProjectilePosPacket extends Packet{

    private final int x;
    private final int y;

    public ProjectilePosPacket(int x, int y) {
        super(PacketType.projectilePos);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}


