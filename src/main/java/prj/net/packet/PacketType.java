package prj.net.packet;

public enum PacketType {
    // control packets
    custom("Custom Packet"),
    multiPacket("Multi Packet"),

    // endpoint packets
    logout("Logout Packet"),
    serverShutdown("Server Shutdown Packet"),

    // world packets
    worldState("World State Packet"),

    // player packets
    playerMove("Player Move Packet"),
    playerPos("Player Position Packet"),


    // request packets
    getWorldState("World State Request Packet"),
    getPlayerPos("Player Position Request Packet"),


    // game state packets
    scrDimension("Screen Dimension"),
    passData("Pass Data");

    private final String packetName;

    PacketType(String packetName){
        this.packetName = packetName;
    }

    public String getPacketName() {
        return packetName;
    }
}
