package com.company;

import java.awt.*;

public class MouseCursor {

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isInRange;

    public MouseCursor(int x, int y, boolean isInRange) {
        this.x = x;
        this.y = y;
        this.width = 20;
        this.height = 20;
        this.isInRange = isInRange;
    }

    /* Settery i gettery */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isInRange() {
        return isInRange;
    }

    public void setInRange(boolean inRange) {
        isInRange = inRange;
    }
    /* ****** */

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
