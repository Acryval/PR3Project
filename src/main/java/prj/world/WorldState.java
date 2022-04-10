package prj.world;

// Klasa przechowująca wszystkie zmienne składające się na świat i odróżniające światy od siebie
// Jedyne metody to gettery i settery ( !! wszystkie synchronizowane ze względu na zmienną !! )

import prj.net.packet.system.Packable;

public class WorldState implements Packable {

    public WorldState() {
        //TODO set initial values
    }

    public WorldState(WorldState initialState) {
        set(initialState);
    }

    public void set(WorldState state){
    }
}
