package com.company;

import javax.swing.*;
import java.awt.*;

public class DefaultBreakableWall extends Wall {

    public DefaultBreakableWall(int x, int y, GamePanel panel) {
        super(x, y, 50, 50, 100, true, true, panel);
        super.setHitbox(new Rectangle(x, y, 50, 50));
        loadImage();
    }

    @Override
    public void loadImage() {
        ImageIcon ii = new ImageIcon("BlockPrototype.png");
        super.getImage().add(ii.getImage());

        ii = new ImageIcon("BlockPrototypeBrokenStage1.png");
        super.getImage().add(ii.getImage());

        ii = new ImageIcon("BlockPrototypeBrokenStage2.png");
        super.getImage().add(ii.getImage());

        ii = new ImageIcon("BlockPrototypeBrokenStage3.png");
        super.getImage().add(ii.getImage());

        ii = new ImageIcon("BlockPrototypeBrokenStage4.png");
        super.getImage().add(ii.getImage());
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (super.isCollision()) {
            /*
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, width, height);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(x + 1, y + 1, width - 1, height - 1);
            */
            if (this.getDurability() < 20) {
                g2d.drawImage(super.getImage().get(4), super.getX(), super.getY(), super.getPanel());
            } else if (this.getDurability() < 40) {
                g2d.drawImage(super.getImage().get(3), super.getX(), super.getY(), super.getPanel());
            } else if (this.getDurability() < 60) {
                g2d.drawImage(super.getImage().get(2), super.getX(), super.getY(), super.getPanel());
            } else if (this.getDurability() < 80) {
                g2d.drawImage(super.getImage().get(1), super.getX(), super.getY(), super.getPanel());
            } else if (this.getDurability() <= 100) {
                g2d.drawImage(super.getImage().get(0), super.getX(), super.getY(), super.getPanel());
            }
        }
    }
}
