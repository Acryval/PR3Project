package prj.world;

// Klasa przechowująca wszystkie zmienne składające się na świat i odróżniające światy od siebie
// Jedyne metody to gettery i settery ( !! wszystkie typu synchronized !! )

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

public class WorldState extends Packet {
    //TODO put relevant data into worldstate

    public WorldState() {
        //TODO set initial values
    }

    public WorldState(WorldState initialState) {
        //TODO copy data from initialState
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.WorldState;
    }

    @Override
    public boolean expectsAnswer() {
        return false;
    }
}
