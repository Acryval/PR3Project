package prj.net.packet.gamestate;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

import java.awt.*;

public class ScreenDimensionPacket extends Packet {
    private final Dimension screenDimension;
    public ScreenDimensionPacket(int width, int height) {
        super(PacketType.scrDimension);
        screenDimension = new Dimension(width, height);
    }

    public Dimension getScreenDimension() {
        return screenDimension;
    }
}
