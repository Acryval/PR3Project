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
import prj.wall.DefaultTransparentWall;
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

            for (int i = -50; i < 800; i += 50) {
                state.wallsByCords.put(new Point(i, 1000), new DefaultSpikeWall(i, 1000));
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
                            state.wallsByCords.put(p.getPos(), p.getWall());
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
                        out.addToSend(new BlockPlacedPacket(p.getPos(), p.getWall()));
                    }
                }
                case blockPlaced -> {
                    BlockPlacedPacket p = (BlockPlacedPacket) data;
                    boolean isBlockNeighbour = false;
                    int cellCordsX = p.getPos().x;
                    int cellCordsY = p.getPos().y;

                    synchronized (state) {
                        Wall w = state.wallsByCords.get(new Point(cellCordsX - 50, cellCordsY));
                        if (w != null && w.isCollision()) isBlockNeighbour = true;
                        else w = state.wallsByCords.get(new Point(cellCordsX + 50, cellCordsY));
                        if (!isBlockNeighbour && w != null && w.isCollision()) isBlockNeighbour = true;
                        else w = state.wallsByCords.get(new Point(cellCordsX, cellCordsY - 50));
                        if (!isBlockNeighbour && w != null && w.isCollision()) isBlockNeighbour = true;
                        else w = state.wallsByCords.get(new Point(cellCordsX, cellCordsY + 50));
                        if (!isBlockNeighbour && w != null && w.isCollision()) isBlockNeighbour = true;

                        boolean isPlayerCollision = false;
                        Rectangle rect = new Rectangle(cellCordsX, cellCordsY, 50, 50);
                        for (Map.Entry<String, Player> pl : state.players.entrySet()) {
                            if(pl.getValue().getHitbox().intersects(rect)){
                                isPlayerCollision = true;
                                break;
                            }
                        }

                        if (isBlockNeighbour && !isPlayerCollision) {
                            state.wallsByCords.put(p.getPos(), p.getWall());
                            out.addToBroadcast(p);
                        }else{
                            out.addToSend(new BlockBrokenPacket(p.getPos(), p.getWall()));
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
