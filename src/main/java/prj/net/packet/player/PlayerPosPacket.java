package prj.net.packet.player;

import org.joml.Vector2i;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class PlayerPosPacket extends Packet {
    @Override
    public PacketType getPacketType() {
        return PacketType.playerPos;
    }

    private final Vector2i pos;

    public PlayerPosPacket(Vector2i pos) {
        this.pos = pos;
    }

    public Vector2i getPos() {
        return pos;
    }
}
