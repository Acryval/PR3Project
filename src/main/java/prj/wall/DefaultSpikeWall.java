package prj.wall;

import javax.swing.*;
import java.awt.*;

public class DefaultSpikeWall extends Wall{

    public DefaultSpikeWall(int x, int y) {
        super(x, y, 50, 50, false, true, true, "DefaultSpikeWall");
        super.setHitbox(new Rectangle(x, y, 50, 50));
        this.setDamage(50);
        loadImage();
    }
    @Override
    public void loadImage() {
        super.getImage().add("SpikePrototype");
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(super.getImage(0), super.getX(), super.getY(), null);
    }
}
