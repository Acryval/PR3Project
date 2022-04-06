package prj.world;

// Klasa przechowująca metody zależne od aktualnego stanu świata i bezpośrednio nań wpływające

import prj.net.packet.PacketDataType;

public class World {
    private final WorldState currentState;

    public World() {
        currentState = new WorldState();
        //TODO generate new world
    }

    public World(World localWorld) {
        this.currentState = new WorldState(localWorld.getCurrentState());
    }

    public WorldState getCurrentState() {
        return currentState;
    }

    public <T extends PacketDataType> void applyPacketData(T data){
        //TODO applyPacketCode --PacketDataType dependent--
    }

    public void updateState(double dtime){
        //TODO update world state
    }

    //TODO make relevant methods to change the world state
}
