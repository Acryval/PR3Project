package prj.world;

import prj.entity.Player;
import prj.net.packet.Packable;
import prj.net.packet.PacketElement;
import prj.wall.Wall;

import java.awt.*;
import java.util.HashMap;

public class WorldState implements Packable {
    @PacketElement
    public String worldName;
    @PacketElement
    public HashMap<String, Player> players;
    @PacketElement
    public HashMap<Point, Wall> wallsByCords;

    public WorldState() {
        worldName = "";
        wallsByCords = new HashMap<>();
        players = new HashMap<>();
    }

    public WorldState(WorldState initialState) {
        this();
        set(initialState);
    }

    public void set(WorldState state){
        worldName = state.worldName;
        wallsByCords = state.wallsByCords;
        players = state.players;
    }
}
