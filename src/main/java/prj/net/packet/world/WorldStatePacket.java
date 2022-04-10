package prj.net.packet.world;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.net.packet.system.CustomPacket;
import prj.world.WorldState;

public class WorldStatePacket extends CustomPacket {
    @Override
    public PacketType getPacketType() {
        return PacketType.worldState;
    }

    public WorldStatePacket(WorldState worldState) {
        super(worldState);
    }
}
