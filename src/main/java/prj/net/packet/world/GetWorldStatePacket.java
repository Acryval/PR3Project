package prj.net.packet.world;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class GetWorldStatePacket extends Packet {
    public GetWorldStatePacket() {
        super(PacketType.getWorldState);
        expect(PacketType.worldState);
    }
}
