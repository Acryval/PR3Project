package prj.net.packet.system;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class LoginPacket extends Packet {
    private final String username;
    public LoginPacket(String username) {
        super(PacketType.login);
        this.username = username;
        expect(PacketType.worldState);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getName(){
        return super.getName() + "{" + getUsername() + "}";
    }
}
