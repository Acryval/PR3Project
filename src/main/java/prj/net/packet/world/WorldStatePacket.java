package prj.net.packet.world;

import prj.net.packet.CustomPacket;
import prj.net.packet.PacketType;
import prj.world.WorldState;

public class WorldStatePacket extends CustomPacket {
    public WorldStatePacket(WorldState worldState) {
        super(PacketType.worldState, worldState);
    }
}
