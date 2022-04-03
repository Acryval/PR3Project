package com.company;

import java.awt.*;

public class Wall {

    int x;
    int y;
    int width;
    int height;
    Rectangle hitbox;

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitbox = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + 1, y + 1, width - 2, height - 2);
    }
}
