package prj.net.packet.world;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class GetWorldStatePacket extends Packet {
    @Override
    public PacketType getPacketType() {
        return PacketType.getWorldState;
    }

    public GetWorldStatePacket() {
        expectedReturnPackets.add(PacketType.worldState);
    }
}
