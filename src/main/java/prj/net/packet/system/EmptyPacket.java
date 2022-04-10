package prj.net.packet.system;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class EmptyPacket extends Packet {
    @Override
    public PacketType getPacketType() {
        return PacketType.empty;
    }
}
