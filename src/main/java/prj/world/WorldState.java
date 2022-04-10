package prj.world;

// Klasa przechowująca wszystkie zmienne składające się na świat i odróżniające światy od siebie
// Jedyne metody to gettery i settery ( !! wszystkie synchronizowane ze względu na zmienną !! )

import java.io.Serializable;

public class WorldState implements Serializable {
    //TODO put relevant data into worldstate

    public WorldState() {
        //TODO set initial values
    }

    public WorldState(WorldState initialState) {
        //TODO copy data from initialState
    }
}
