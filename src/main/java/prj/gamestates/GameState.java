package prj.gamestates;

import prj.net.packet.Packet;
import prj.net.packet.gamestate.PassDataPacket;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class GameState {
    public GameState() {}

    public GameState init(List<Packet> dataIn){
        processPackets(dataIn);
        return this;
    }

    public List<Packet> passRest(List<Packet> packets){
        int pindx = 0;
        for(; pindx < packets.size(); pindx++){
            if(packets.get(pindx) instanceof PassDataPacket){
                break;
            }
        }
        return packets.subList(pindx, packets.size());
    }

    public abstract void processPackets(List<Packet> dataIn);
    public abstract List<Packet> unload(List<Packet> endData);
    public abstract void setActions(InputMap im, ActionMap am);
    public abstract void draw(Graphics2D g, int width, int height);

    public GameState init(Packet...dataIn){
        return init(List.of(dataIn));
    }
    public void processPackets(Packet...dataIn){
        processPackets(List.of(dataIn));
    }
    public List<Packet> unload(Packet...endData){
        return unload(List.of(endData));
    }
}
