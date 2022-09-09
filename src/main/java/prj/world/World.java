package prj.world;

import prj.ClientThread;
import prj.ServerThread;
import prj.entity.Player;
import prj.item.Block;
import prj.item.ItemBar;
import prj.item.Pickaxe;
import prj.log.Logger;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.net.packet.system.LoginPacket;
import prj.net.packet.system.LogoutPacket;
import prj.net.packet.world.WorldStatePacket;
import prj.wall.DefaultBreakableWall;
import prj.wall.DefaultTransparentWall;
import prj.wall.Wall;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class World {
    private final Logger logger = new Logger("");
    private final WorldState state;
    private Player localPlayer;
    private final boolean isServerWorld;

    public World(ServerThread serverInstance) {
        logger.setName("Server world").dbg("init start");
        if(serverInstance == null) throw new IllegalArgumentException("Cannot create a server world without a valid server");

        this.isServerWorld = true;
        this.localPlayer = null;
        this.state = new WorldState(ClientThread.instance.getWorld().getState());

        logger.dbg("init end");
    }

    public World() {
        logger.setName("Client world").dbg("init start");

        this.isServerWorld = false;
        this.state = new WorldState();

        generateWorld();

        this.logger.dbg("init end");
    }

    public WorldState getState() {
        return state;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public void generateWorld(){
        ItemBar itemBar = new ItemBar(10, 10, (1400 - 50) / 2, (900 - 100) / 2, 10, 50, 50, 10, 0);
        Player player = new Player((1400 - 50) / 2, (900 - 100) / 2, itemBar);
        player.getItemBar().addItem(new Pickaxe());
        player.getItemBar().addItem(new Block());
        player.setLoggedIn(true);
        localPlayer = player;

        synchronized (state) {
            for (int i = -500; i < 1300; i += 50) {
                for (int j = -500; j < 1100; j += 50) {
                    state.wallsByCords.put(new Point(i, j), new DefaultTransparentWall(i, j));
                }
            }
            for (int i = -50; i < 800; i += 50) {
                state.wallsByCords.put(new Point(i, 600), new DefaultBreakableWall(i, 600));
            }

            state.wallsByCords.put(new Point(0, 550), new DefaultBreakableWall(0, 550));
            state.wallsByCords.put(new Point(-50, 500), new DefaultBreakableWall(-50, 500));
            state.wallsByCords.put(new Point(-100, 450), new DefaultBreakableWall(-100, 450));
            state.wallsByCords.put(new Point(-150, 400), new DefaultBreakableWall(-150, 400));
            state.wallsByCords.put(new Point(200, 550), new DefaultBreakableWall(200, 550));
            state.wallsByCords.put(new Point(250, 550), new DefaultBreakableWall(250, 550));

            state.wallsByCords.put(new Point(500, 400), new DefaultBreakableWall(500, 400));
            state.wallsByCords.put(new Point(550, 400), new DefaultBreakableWall(550, 400));
        }
    }

    public boolean applyPacketData(List<Packet> packets){
        if(packets.size() == 0) return false;

        boolean logout = false;
        for(Packet data : packets) {
            switch (data.getType()) {
                case login -> {
                    if(isServerWorld){
                        LoginPacket p = (LoginPacket)data;
                        if(!p.getUsername().equals(ClientThread.instance.getUsername())) {
                            if (!state.players.containsKey(p.getUsername())) {
                                ItemBar itemBar = new ItemBar(10, 10, (1400 - 50) / 2, (900 - 100) / 2, 10, 50, 50, 10, 0);
                                Player player = new Player((1400 - 50) / 2, (900 - 100) / 2, itemBar);
                                player.getItemBar().addItem(new Pickaxe());
                                player.getItemBar().addItem(new Block());
                                synchronized (state) {
                                    state.players.put(p.getUsername(), player);
                                }
                            }
                            state.players.get(p.getUsername()).setLoggedIn(true);
                        }
                    }
                }
                case logout -> {
                    if(isServerWorld) {
                        LogoutPacket p = (LogoutPacket)data;
                        if(state.players.containsKey(p.getUsername())){
                            state.players.get(p.getUsername()).setLoggedIn(false);
                        }
                        logout = true;
                    }
                }
                case serverShutdown -> {
                    if(!isServerWorld){
                        logout = true;
                    }
                }
                case worldState -> ((WorldStatePacket)data).unpackInto(state);
                default -> {}
            }
        }

        return logout;
    }

    public List<Packet> preparePackets(List<PacketType> returnPacketTypes){
        List<Packet> out = new ArrayList<>();

        for (PacketType pt : returnPacketTypes) {
            switch (pt) {
                case worldState -> out.add(new WorldStatePacket(state));
                default -> {}
            }
        }

        return out;
    }

    public void updateState(double dtime){
        List<PacketType> updatePackets = new ArrayList<>();

        synchronized (state) {
            if(!isServerWorld){
                localPlayer.update(state, dtime);
            }

            for (Map.Entry<String, Player> player : state.players.entrySet()) {
                player.getValue().update(state, dtime);
            }
        }

        if(isServerWorld) {

            ServerThread.instance.getNetworkManager().broadcast(preparePackets(updatePackets));
        }
    }

    public void draw(Graphics2D g){
        if(!isServerWorld) {
            localPlayer.draw(g);
            synchronized (state) {
                for (Map.Entry<String, Player> player : state.players.entrySet()) {
                    player.getValue().draw(g);
                }
                for (Map.Entry<Point, Wall> wall : state.wallsByCords.entrySet()) {
                    wall.getValue().draw(g);
                }
            }
        }
    }

    public Player getPlayer(String username){
        synchronized (state){
            return state.players.get(username);
        }
    }
}
