package prj.net.packet;

public enum PacketType {
    empty("empty"),
    custom("custom"),
    login("login"),
    logout("logout"),
    multiPacket("multiPacket"),
    serverShutdown("serverShutdown"),

    getWorldState("getWorldState"),
    worldState("worldState"),

    playerMove("playerMove"),
    getPlayerPos("getPlayerPos"),
    playerPos("playerPos");

    private final String packetName;

    PacketType(String packetName){
        this.packetName = packetName;
    }

    public String getPacketName() {
        return packetName;
    }
}
