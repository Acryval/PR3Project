package prj.world;

import prj.entity.Player;
import prj.net.packet.Packable;
import prj.net.packet.PacketElement;
import prj.wall.Wall;

import java.awt.*;
import java.util.HashMap;

public class WorldState implements Packable {
    @PacketElement
    private HashMap<String, Player> players;
    @PacketElement
    private HashMap<Point, Wall> wallsByCords;

    public WorldState() {
        wallsByCords = new HashMap<>();
        players = new HashMap<>();
    }

    public WorldState(WorldState initialState) {
        this();
        set(initialState);
    }

    public void set(WorldState state){
        wallsByCords = state.wallsByCords;
        players = state.players;
    }

    public HashMap<Point, Wall> getWallsByCords() {
        return wallsByCords;
    }

    public void setWallsByCords(HashMap<Point, Wall> wallsByCords) {
        this.wallsByCords = wallsByCords;
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }
}
