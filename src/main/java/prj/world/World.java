package prj.world;

// Klasa przechowująca metody zależne od aktualnego stanu świata i bezpośrednio nań wpływające

import prj.ClientThread;
import prj.ServerThread;
import prj.log.Logger;
import prj.net.ConnectionListener;
import prj.net.packet.GetWorldStatePacket;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class World {
    private final Logger logger = new Logger("");
    private final WorldState currentState;
    private final ConnectionListener connectionListener;
    private final ServerThread serverInstance;
    private final ClientThread clientInstance;

    public World(World localWorld, int listenerPort, int listenerBacklog, ServerThread serverInstance) throws IOException {
        logger.setName("Server world").dbg("init start");

        this.serverInstance = serverInstance;
        this.clientInstance = null;

        this.connectionListener = new ConnectionListener(this, listenerPort, listenerBacklog);
        this.currentState = new WorldState(localWorld.getCurrentState());

        connectionListener.start();

        logger.dbg("init end");
    }

    public World(int listenerPort, int listenerBacklog, ClientThread clientInstance) throws IOException {
        logger.setName("Client world").dbg("init start");

        this.serverInstance = null;
        this.clientInstance = clientInstance;

        this.connectionListener = new ConnectionListener(this, listenerPort, listenerBacklog);
        this.currentState = new WorldState();

        generateWorld();

        connectionListener.start();

        this.logger.dbg("init end");
    }

    public WorldState getCurrentState() {
        return currentState;
    }

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public ServerThread getServerInstance() {
        return serverInstance;
    }

    public ClientThread getClientInstance() {
        return clientInstance;
    }

    public boolean isServerWorld(){
        return serverInstance != null;
    }

    public void generateWorld(){
        //TODO generate new world
    }

    public void applyPacketData(Packet data){
        if(data == null) return;
        //TODO applyPacketCode --PacketType dependent--
    }

    public List<Packet> preparePackets(List<PacketType> returnPacketTypes){
        //TODO prepare and send all expected packets
        List<Packet> out = new ArrayList<>();

        for(PacketType pt : returnPacketTypes){
            switch (pt){
                case getWorldState -> out.add(new GetWorldStatePacket(currentState));
                default -> {}
            }
        }

        return out;
    }

    public void updateState(double dtime){
        //TODO update world state
    }

    //TODO make relevant methods to change the world state
}
