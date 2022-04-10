package prj.world;

// Klasa przechowująca metody zależne od aktualnego stanu świata i bezpośrednio nań wpływające

import prj.ClientThread;
import prj.ServerThread;
import prj.log.Logger;
import prj.net.ConnectionListener;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.net.packet.system.LoginPacket;
import prj.net.packet.system.LogoutPacket;
import prj.net.packet.world.WorldStatePacket;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {
    private final Logger logger = new Logger("");
    private final WorldState state;
    private final ConnectionListener connectionListener;
    private final ServerThread serverInstance;
    private final ClientThread clientInstance;

    public World(World localWorld, int listenerPort, int listenerBacklog, ServerThread serverInstance) {
        logger.setName("Server world").dbg("init start");

        this.serverInstance = serverInstance;
        this.clientInstance = null;

        this.connectionListener = new ConnectionListener(this, listenerPort, listenerBacklog);
        this.state = new WorldState(localWorld.getState());

        connectionListener.start();

        logger.dbg("init end");
    }

    public World(int listenerPort, int listenerBacklog, ClientThread clientInstance) {
        logger.setName("Client world").dbg("init start");

        this.serverInstance = null;
        this.clientInstance = clientInstance;

        this.connectionListener = new ConnectionListener(this, listenerPort, listenerBacklog);
        this.state = new WorldState();

        generateWorld();

        connectionListener.start();

        this.logger.dbg("init end");
    }

    public WorldState getState() {
        return state;
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
    }

    public void applyPacketData(Packet data){
        if(data == null) return;
        //TODO applyPacketCode --PacketType dependent--

        switch (data.getPacketType()){
            case login -> {
                if(isServerWorld()){
                    LoginPacket p = (LoginPacket) data;
                    getServerInstance().getNetworkManager().clientLogin(p.getClientAddress());
                }
            }
            case logout -> {
                if(isServerWorld()){
                    getServerInstance().getNetworkManager().clientLogout(((LogoutPacket)data).getClientAddress());
                }
            }
            case serverShutdown -> {
                if(!isServerWorld()){
                    getClientInstance().getNetworkManager().shutdown();
                }
            }
            case worldState -> {
                synchronized(state){
                    ((WorldStatePacket)data).unpacInto(state);
                }
            }
            case playerMove -> {
            }
            case playerPos -> {
            }
            default -> {}
        }
    }

    public List<Packet> preparePackets(List<PacketType> returnPacketTypes){
        //TODO prepare and send all expected packets
        List<Packet> out = new ArrayList<>();

        for(PacketType pt : returnPacketTypes){
            switch (pt){
                case worldState -> out.add(new WorldStatePacket(state));
                case getPlayerPos -> {
                }
                default -> {}
            }
        }

        return out;
    }

    public void updateState(double dtime){
        if(isServerWorld()){
            WorldState temp = new WorldState(state);
            List<Packet> updatePackets = new ArrayList<>();


            send(updatePackets);
        }
    }

    public void draw(Graphics2D g){
    }

    public void send(List<Packet> packets){
        if(isServerWorld()){
            getServerInstance().getNetworkManager().broadcast(packets);
        }else{
            getClientInstance().getNetworkManager().send(packets);
        }
    }

    //TODO make relevant methods to change the world state
}
