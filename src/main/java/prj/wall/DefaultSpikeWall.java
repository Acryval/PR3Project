package com.company;

import javax.swing.*;
import java.awt.*;

public class DefaultSpikeWall extends Wall{

    public DefaultSpikeWall(int x, int y, GamePanel panel) {
        super(x, y, 50, 50, false, true, false, panel);
        super.setHitbox(new Rectangle(x, y, 50, 50));
        this.setDamage(100);
        loadImage();
    }
    @Override
    public void loadImage() {
        ImageIcon ii = new ImageIcon("SpikePrototype.png");
        super.getImage().add(ii.getImage());
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(this.getImage().get(0), super.getX(), super.getY(), super.getPanel());
    }
}
