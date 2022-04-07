package prj.net.packet;

public enum PacketType {
    empty("empty"),
    login("login"),
    logout("logout"),
    serverShutdown("serverShutdown"),
    multiPacket("multiPacket"),
    getWorldState("getWorldState");

    private final String packetName;

    PacketType(String packetName){
        this.packetName = packetName;
    }

    public String getPacketName() {
        return packetName;
    }
}
