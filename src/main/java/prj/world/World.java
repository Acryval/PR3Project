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
import prj.net.packet.entity.player.GetPlayerPosPacket;
import prj.net.packet.entity.player.PlayerMovePacket;
import prj.net.packet.entity.player.PlayerPosPacket;
import prj.net.packet.system.LoginPacket;
import prj.net.packet.system.LogoutPacket;
import prj.net.packet.world.BlockBrokenPacket;
import prj.net.packet.world.BlockPlacedPacket;
import prj.net.packet.world.WorldStatePacket;
import prj.wall.DefaultBreakableWall;
import prj.wall.DefaultSpikeWall;
<<<<<<< HEAD
=======
import prj.wall.DefaultUnbreakableWall;
>>>>>>> 74b8399393bb14de84ef1bd62fcf65ba183c3de0
import prj.wall.Wall;

import java.awt.*;
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

    public World(String name) {
        logger.setName("Client world").dbg("init start");

        this.isServerWorld = false;
        this.state = new WorldState();
        this.state.worldName = name;

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
<<<<<<< HEAD
            for (int i = -50; i < 800; i += 50) {
                state.wallsByCords.put(new Point(i, 600), new DefaultBreakableWall(i, 600));
=======
            for(int i = -500 ; i < 0 ; i += 50) {
                for(int j = -3000 ; j < 4400 ; j += 50) {
                    state.wallsByCords.put(new Point(j, i), new DefaultUnbreakableWall(j, i));
                }
            }
            for(int i = 0 ; i < 1500 ; i += 50) {
                for(int j = -3000 ; j < -2000 ; j += 50) {
                    state.wallsByCords.put(new Point(j, i), new DefaultUnbreakableWall(j, i));
                    state.wallsByCords.put(new Point(j + 6400, i), new DefaultUnbreakableWall(j + 6400, i));
                }
            }
            for(int i = 1500 ; i < 2000 ; i += 50) {
                for(int j = -3000 ; j < 4400 ; j += 50) {
                    state.wallsByCords.put(new Point(j, i), new DefaultUnbreakableWall(j, i));
                }
            }
            for(int i = -2000 ; i < 3400 ; i += 50) {
                state.wallsByCords.put(new Point(i, 1450), new DefaultSpikeWall(i, 1450));
>>>>>>> 74b8399393bb14de84ef1bd62fcf65ba183c3de0
            }


<<<<<<< HEAD
            state.wallsByCords.put(new Point(500, 400), new DefaultBreakableWall(500, 400));
            state.wallsByCords.put(new Point(550, 400), new DefaultBreakableWall(550, 400));

            for (int i = -50; i < 800; i += 50) {
                state.wallsByCords.put(new Point(i, 1000), new DefaultSpikeWall(i, 1000));
=======
            for (int i = -1950; i < 3350; i += 50) {
                state.wallsByCords.put(new Point(i, 600), new DefaultBreakableWall(i, 600));
>>>>>>> 74b8399393bb14de84ef1bd62fcf65ba183c3de0
            }
        }
    }

    public UserSpecificData applyPacketData(List<Packet> packets){
        UserSpecificData res = new UserSpecificData();
        if(packets.size() == 0) return res;

        for(Packet data : packets) {
            res.addExpectedPackets(data.getExpectedReturnPackets());
            switch (data.getType()) {
                case login -> {
                    LoginPacket p = (LoginPacket)data;
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
                    if(isServerWorld) {
                        res.storePacket(p);
                    }
                }
                case logout -> {
                    LogoutPacket p = (LogoutPacket)data;
                    if(state.players.containsKey(p.getUsername())){
                        state.players.get(p.getUsername()).setLoggedIn(false);
                    }
                    if(isServerWorld) {
                        res.setLogout(true);
                        res.storePacket(p);
                    }
                }
                case serverShutdown -> {
                    if(!isServerWorld){
                        res.setLogout(true);
                    }
                }
                case worldState -> {
                    synchronized (state) {
                        ((WorldStatePacket) data).unpackInto(state);
                    }
                }
                case playerMove -> {
                    if(isServerWorld) {
                        PlayerMovePacket p = (PlayerMovePacket) data;
                        if (state.players.containsKey(p.getUsername())) {
                            switch (p.getDir()) {
                                case UP -> state.players.get(p.getUsername()).setKeyUp(p.getValue());
                                case LEFT -> state.players.get(p.getUsername()).setKeyLeft(p.getValue());
                                case RIGHT -> state.players.get(p.getUsername()).setKeyRight(p.getValue());
                                default -> {
                                }
                            }
                        }
                    }
                }
                case playerPos -> {
                    if(!isServerWorld){
                        PlayerPosPacket p = (PlayerPosPacket) data;
                        if(p.getUsername().equals(ClientThread.instance.getUsername())){
                            localPlayer.setPos(p.getX(), p.getY());
                        }else{
                            if(state.players.containsKey(p.getUsername())){
                                state.players.get(p.getUsername()).setPos(p.getX(), p.getY());
                            }
                        }

                    }
                }
                case blockBroken -> {
                    if(isServerWorld){
                        res.storePacket(data);
                    }else{
                        BlockBrokenPacket p = (BlockBrokenPacket) data;
                        synchronized (state){
                            state.wallsByCords.remove(p.getPos());
                        }
                    }
                }
                case blockPlaced -> {
                    if(isServerWorld){
                        res.storePacket(data);
                    }else{
                        BlockPlacedPacket p = (BlockPlacedPacket) data;
                        synchronized (state){
                            if(p.getNewWall() == null){
                                state.wallsByCords.remove(p.getPos());
                            }else {
                                state.wallsByCords.put(p.getPos(), p.getNewWall());
                            }
                        }
                    }
                }
                case getPlayerPos -> {
                    if(isServerWorld){
                        res.storePacket(data);
                    }
                }
                default -> {}
            }
        }

        return res;
    }

    public PreparedPackets preparePackets(UserSpecificData res){
        PreparedPackets out = new PreparedPackets();

        for(Packet data : res.getStoredPackets()){
            switch(data.getType()){
                case login, logout, playerPos -> out.addToBroadcast(data);
                case blockBroken -> {
                    BlockBrokenPacket p = (BlockBrokenPacket) data;
                    if(state.wallsByCords.containsKey(p.getPos()) && state.wallsByCords.get(p.getPos()).getType().equals(p.getWall().getType())){
                        synchronized (state) {
                            state.wallsByCords.remove(p.getPos());
                        }
                        out.addToBroadcast(p);
                    }else{
                        out.addToSend(new BlockPlacedPacket(p.getPos(), null, p.getWall()));
                    }
                }
                case blockPlaced -> {
                    BlockPlacedPacket p = (BlockPlacedPacket) data;
                    boolean isBlockNeighbour = false;
                    boolean isPlayerCollision = false;
                    int cellCordsX = p.getPos().x;
                    int cellCordsY = p.getPos().y;

                    synchronized (state) {
                        Wall w = state.wallsByCords.get(p.getPos());

                        if(w == null) {
                            w = state.wallsByCords.get(new Point(cellCordsX - 50, cellCordsY));
                            if (w != null && w.isCollision()) isBlockNeighbour = true;
                            else w = state.wallsByCords.get(new Point(cellCordsX + 50, cellCordsY));
                            if (!isBlockNeighbour && w != null && w.isCollision()) isBlockNeighbour = true;
                            else w = state.wallsByCords.get(new Point(cellCordsX, cellCordsY - 50));
                            if (!isBlockNeighbour && w != null && w.isCollision()) isBlockNeighbour = true;
                            else w = state.wallsByCords.get(new Point(cellCordsX, cellCordsY + 50));
                            if (!isBlockNeighbour && w != null && w.isCollision()) isBlockNeighbour = true;

                            Rectangle rect = new Rectangle(cellCordsX, cellCordsY, 50, 50);
                            for (Map.Entry<String, Player> pl : state.players.entrySet()) {
                                if (pl.getValue().getHitbox().intersects(rect)) {
                                    isPlayerCollision = true;
                                    break;
                                }
                            }
                        }

                        if (isBlockNeighbour && !isPlayerCollision) {
                            state.wallsByCords.put(p.getPos(), p.getNewWall());
                            out.addToBroadcast(p);
                        }else{
                            out.addToSend(new BlockPlacedPacket(p.getPos(), p.getNewWall(), p.getOldWall()));
                        }
                    }
                }
                case getPlayerPos -> {
                    GetPlayerPosPacket p = (GetPlayerPosPacket) data;
                    if(state.players.containsKey(p.getUsername())){
                        Player pl = state.players.get(p.getUsername());
                        out.addToSend(new PlayerPosPacket(p.getUsername(), pl.getX(), pl.getY()));
                    }
                }
                default -> {}
            }
        }

        for (PacketType pt : res.getExpectedPackets()) {
            switch (pt) {
                case worldState -> out.addToSend(new WorldStatePacket(state));
                default -> {}
            }
        }

        return out;
    }

    public void updateState(double dtime){
        UserSpecificData data = new UserSpecificData();

        synchronized (state) {
            if(isServerWorld){
                for (Map.Entry<String, Player> player : state.players.entrySet()) {
                    Point p = new Point(player.getValue().getX(), player.getValue().getY());
                    player.getValue().update(state, dtime);
                    if(player.getValue().getX() != p.x || player.getValue().getY() != p.y){
                        data.storePacket(new PlayerPosPacket(player.getKey(), player.getValue().getX(), player.getValue().getY()));
                    }
                }
            }else{
                localPlayer.update(state, dtime);
            }
        }

        if(isServerWorld) {
            ServerThread.instance.getNetworkManager().broadcast(preparePackets(data).getToBroadcast());
        }
    }

    public void draw(Graphics2D g){
        if(!isServerWorld) {
            localPlayer.draw(g);
            synchronized (state) {
                for (Map.Entry<String, Player> player : state.players.entrySet()) {
                    if(!player.getKey().equals(ClientThread.instance.getUsername()))
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
