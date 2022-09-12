package prj.net.packet.system;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class LogoutPacket extends Packet {
    private final String username;
    public LogoutPacket(String username) {
        super(PacketType.logout);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getName(){
        return super.getName() + "{" + getUsername() + "}";
    }
}
