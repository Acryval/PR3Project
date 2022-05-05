package prj.world;

import prj.entity.Player;
import prj.net.packet.Packable;
import prj.net.packet.PacketElement;

public class WorldState implements Packable {
    @PacketElement
    private final Player player;

    public WorldState() {
        player = new Player();
    }

    public WorldState(WorldState initialState) {
        this();
        set(initialState);
    }

    public void set(WorldState state){
        player.setPos(state.player.getPos());
    }

    public Player getPlayer() {
        return player;
    }
}
