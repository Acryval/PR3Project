package com.company;

import java.awt.*;

public class MouseCursor {

    private int x;
    private int y;
    private int width;
    private int height;
    boolean isInRange;

    public MouseCursor(int x, int y, int width, int height, boolean isInRange) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isInRange = isInRange;
    }

    // do poprawenia 
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x - width / 2, y - height / 2, width + 2, height + 2);
        if(isInRange) {
            g2d.setColor(Color.GREEN);
        }
        else {
            g2d.setColor(Color.RED);
        }
        g2d.fillOval(x + 1 - width / 2, y + 1 - height / 2, width, height);
    }
}
