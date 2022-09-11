package prj.gamestates;

import prj.net.packet.Packet;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class GameState {
    public GameState() {}

    public GameState init(List<Packet> dataIn){
        processPackets(dataIn);
        return this;
    }

    public abstract void processPackets(List<Packet> dataIn);
    public abstract List<Packet> unload(List<Packet> endData);
    public abstract void setActions(InputMap im, ActionMap am);
    public abstract void update(double dt);
    public abstract void draw(Graphics2D g, int width, int height);
    public List<Packet> unload(Packet...endData){
        return unload(List.of(endData));
    }
}
