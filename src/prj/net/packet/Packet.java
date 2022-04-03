package prj.net.packet;

import java.nio.ByteBuffer;

public class Packet {
    public enum PacketType{
        INVALID,
        ILLEGAL,
        SYNC_STATE
    }

    private final PacketType type;
    protected ByteBuffer data;

    public Packet(PacketType type) {
        this.type = type;
    }
}
