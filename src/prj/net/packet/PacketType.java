package prj.net.packet;

public enum PacketType {
    WorldState("WorldState");

    public final String packetName;

    PacketType(String name){
        this.packetName = name;
    }
}
