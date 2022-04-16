package com.company;

import java.awt.*;
import java.util.Random;

public class Wall {

    int x;
    int y;
    int width;
    int height;
    int durability;
    boolean isBreakable;
    boolean isCollision;
    Rectangle hitbox;

    public Wall(int x, int y, int width, int height, int durabilty, boolean isBreakable, boolean isCollision) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.durability = durability;
        this.isBreakable = isBreakable;
        this.isCollision = isCollision;
        hitbox = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + 1, y + 1, width - 1, height - 1);
    }
}
