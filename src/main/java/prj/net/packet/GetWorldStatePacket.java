package prj.net.packet;

import prj.world.WorldState;

public class GetWorldStatePacket extends Packet{
    @Override
    public PacketType getPacketType() {
        return PacketType.getWorldState;
    }

    private final WorldState worldState;

    public GetWorldStatePacket(WorldState worldState) {
        this.worldState = worldState;
    }

    public WorldState getWorldState() {
        return worldState;
    }
}
