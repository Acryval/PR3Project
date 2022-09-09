package prj.net.packet;

public enum PacketType {
    // control packets
    objectPacket,
    multiPacket,

    // endpoint packets
    login,
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
    passData,
    setUsername,
    startServer,
    connectToServer
}
