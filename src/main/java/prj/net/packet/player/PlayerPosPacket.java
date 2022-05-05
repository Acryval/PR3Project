package prj.net.packet.player;

import org.joml.Vector2d;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class PlayerPosPacket extends Packet {
    private final Vector2d pos;

    public PlayerPosPacket(Vector2d pos) {
        super(PacketType.playerPos);
        this.pos = pos;
    }

    public Vector2d getPos() {
        return pos;
    }
}
