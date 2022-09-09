package prj.net.packet;

public enum PacketType {
    // control packets
    objectPacket,
    multiPacket,

    // endpoint packets
    logout,
    serverShutdown,

    // world packets
    worldState,

    // player packets
    playerMove,
    playerPos,


    // request packets
    getWorldState,
    getPlayerPos,


    // game state packets
    scrDimension,
    passData
}
