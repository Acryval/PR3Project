package prj.world;

// Klasa przechowująca metody zależne od aktualnego stanu świata i bezpośrednio nań wpływające

public class World {
    private WorldState currentState;

    public World(WorldState initialState) {
        this.currentState = new WorldState(initialState);
    }

    public WorldState getCurrentState() {
        return currentState;
    }

    //TODO make relevant methods to change the world state
}
