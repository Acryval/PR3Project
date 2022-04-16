package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Wall {

    int x;
    int y;
    int width;
    int height;
    int durability;
    boolean isBreakable;
    boolean isCollision;
    Rectangle hitbox;
    ArrayList<Image> image = new ArrayList<>();
    GamePanel panel;

    public Wall(int x, int y, int width, int height, int durability, boolean isBreakable, boolean isCollision, GamePanel panel) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.durability = durability;
        this.isBreakable = isBreakable;
        this.isCollision = isCollision;
        hitbox = new Rectangle(x, y, width, height);
        this.panel = panel;
        loadImage();
    }

    private void loadImage() {
        ImageIcon ii = new ImageIcon("BlockPrototype.png");
        image.add(ii.getImage());

        ii = new ImageIcon("BlockPrototypeBrokenStage1.png");
        image.add(ii.getImage());

        ii = new ImageIcon("BlockPrototypeBrokenStage2.png");
        image.add(ii.getImage());

        ii = new ImageIcon("BlockPrototypeBrokenStage3.png");
        image.add(ii.getImage());

        ii = new ImageIcon("BlockPrototypeBrokenStage4.png");
        image.add(ii.getImage());
    }

    public void draw(Graphics2D g2d) {
        if(this.isCollision) {
            /*
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, width, height);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(x + 1, y + 1, width - 1, height - 1);
            */
            if(durability < 20) {
                g2d.drawImage(image.get(4), x, y, panel);
            }
            else if(durability < 40) {
                g2d.drawImage(image.get(3), x, y, panel);
            }
            else if(durability < 60) {
                g2d.drawImage(image.get(2), x, y, panel);
            }
            else if(durability < 80) {
                g2d.drawImage(image.get(1), x, y, panel);
            }
            else if(this.durability <= 100) {
                g2d.drawImage(image.get(0), x, y, panel);
            }
        }
    }
}
