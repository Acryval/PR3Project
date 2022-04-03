package prj.world;

// Klasa przechowująca metody zależne od aktualnego stanu świata i bezpośrednio nań wpływające

public class World {
    private WorldState currentState;

    public WorldState getCurrentState() {
        return currentState;
    }
}
