package prj.world;

import org.joml.Vector2d;
import prj.ClientThread;
import prj.ServerThread;
import prj.log.Logger;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.net.packet.player.PlayerMovePacket;
import prj.net.packet.player.PlayerPosPacket;
import prj.net.packet.world.WorldStatePacket;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {
    private final Logger logger = new Logger("");
    private final WorldState state;
    private final boolean isServerWorld;

    public static final double gravity = 500.0;

    public World(ServerThread serverInstance) {
        logger.setName("Server world").dbg("init start");
        if(serverInstance == null) throw new IllegalArgumentException("Cannot create a server world without a valid server");

        this.isServerWorld = true;
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

    public void generateWorld(){
        state.getPlayer().setPos(new Vector2d(ClientThread.instance.getWidth(), ClientThread.instance.getHeight()).div(2));
    }

    public boolean applyPacketData(List<Packet> packets){
        if(packets.size() == 0) return false;

        boolean logout = false;
        for(Packet data : packets) {
            switch (data.getType()) {
                case logout -> {
                    if(isServerWorld) {
                        logout = true;
                    }
                }
                case serverShutdown -> {
                    if(!isServerWorld){
                        logout = true;
                    }
                }
                case worldState -> ((WorldStatePacket)data).unpackInto(state);
                case playerMove -> {
                    PlayerMovePacket p = (PlayerMovePacket)data;
                    state.getPlayer().setMoving(p.getDirection(), p.value());
                }
                case playerPos -> {
                    PlayerPosPacket p = (PlayerPosPacket)data;
                    state.getPlayer().setPos(p.getPos());
                }
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
                case playerPos -> out.add(new PlayerPosPacket(state.getPlayer().getPos()));
                default -> {}
            }
        }

        return out;
    }

    public void updateState(double dtime){
        List<PacketType> updatePackets = new ArrayList<>();
        Vector2d ppos = new Vector2d(state.getPlayer().getPos());

        state.getPlayer().update(dtime);

        if(isServerWorld) {
            if(!state.getPlayer().getPos().equals(ppos, 0.01)) updatePackets.add(PacketType.playerPos);

            ServerThread.instance.getNetworkManager().broadcast(preparePackets(updatePackets));
        }
    }

    public void draw(Graphics2D g){
        if(!isServerWorld) {
            state.getPlayer().draw(g);
        }
    }
}
