package prj.world;

// Klasa przechowująca metody zależne od aktualnego stanu świata i bezpośrednio nań wpływające

import prj.net.ConnectionListener;
import prj.net.packet.Packet;

import java.io.IOException;
import java.util.Random;

public class World {
    private final WorldState currentState;
    private final ConnectionListener connectionListener;

    public World(World localWorld, int listenerPort, int listenerBacklog) throws IOException {
        connectionListener = new ConnectionListener(this, listenerPort, listenerBacklog);
        this.currentState = new WorldState(localWorld.getCurrentState());
    }

    public World(int listenerPort, int listenerBacklog) throws IOException {
        connectionListener = new ConnectionListener(this, listenerPort, listenerBacklog);
        currentState = new WorldState();
        generateWorld();
    }

    public WorldState getCurrentState() {
        return currentState;
    }

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public static int getRandomPort(){
        return new Random().nextInt(50000, 60000);
    }

    public void generateWorld(){
        //TODO generate new world
    }

    public <T extends Packet> void applyPacketData(T data){
        if(data == null) return;
        //TODO applyPacketCode --PacketDataType dependent--
    }

    public void updateState(double dtime){
        //TODO update world state
    }

    //TODO make relevant methods to change the world state
}
