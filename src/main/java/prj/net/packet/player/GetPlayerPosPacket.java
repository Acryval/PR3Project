package prj.net.packet.player;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class GetPlayerPosPacket extends Packet {
    public GetPlayerPosPacket() {
        super(PacketType.getPlayerPos);
        expect(PacketType.playerPos);
    }
}
