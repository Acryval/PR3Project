package prj.net.packet.world;

import prj.net.packet.ObjectPacket;
import prj.net.packet.PacketType;
import prj.world.WorldState;

public class WorldStatePacket extends ObjectPacket {
    public WorldStatePacket(WorldState worldState) {
        super(PacketType.worldState, worldState);
    }
}
