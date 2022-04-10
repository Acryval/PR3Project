package prj.net.packet.player;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class GetPlayerPosPacket extends Packet {
    @Override
    public PacketType getPacketType() {
        return PacketType.getPlayerPos;
    }

    public GetPlayerPosPacket() {
        expectedReturnPackets.add(PacketType.playerPos);
    }
}
