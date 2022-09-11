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

    // block packets
    blockBroken,
    blockPlaced,

    // request packets
    getWorldState,
    getPlayerPos,


    // game state packets
    scrDimension,
    setUsername,
    setWorldName,
    startServer,
    connectToServer
}
