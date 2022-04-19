package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Player {

    // część pól jest publiczna, bo nie chce mi się robić getterów
    GamePanel panel;
    public int x;
    public int y;
    public int startingPosX;
    public int startingPosY;
    public int width;
    public int height;
    private double velocityX;
    private double velocityY;
    Rectangle hitbox;
    public boolean keyLeft;
    public boolean keyRight;
    public boolean keyUp;
    private Image image;
    ItemBar itemBar;

    public Player(int x, int y, GamePanel panel, ItemBar itemBar) {
        this.x = x;
        this.y = y;
        this.startingPosX = x;
        this.startingPosY = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.panel = panel;
        this.width = 50;
        this.height = 100;
        hitbox = new Rectangle(this.x, this.y, this.width, this.height);
        loadImage();
        this.itemBar = itemBar;
    }

    private void loadImage() {
        ImageIcon ii = new ImageIcon("playerCharacterPrototype.png");
        image = ii.getImage();
    }

    public void move() {
        if(keyLeft && keyRight || !keyLeft && !keyRight) {
            velocityX *= 0.8;
        }
        else if(keyLeft && !keyRight) {
            velocityX--;
        }
        else if(!keyLeft && keyRight) {
            velocityX++;
        }

        if(velocityX > 0 && velocityX < 0.75) velocityX = 0;
        if(velocityX < 0 && velocityX > -0.75) velocityX = 0;
        if(velocityX > 7) velocityX = 7;
        if(velocityX < -7) velocityX = -7;

        if(keyUp) {
            hitbox.y++;
            for(Map.Entry<Point, Wall> wall : panel.wallsByCords.entrySet()) {
                if(wall.getValue().hitbox.intersects(hitbox) && wall.getValue().isCollision) {
                    velocityY = -6;
                }
            }
            hitbox.y--;
        }
        velocityY += 0.3;

        hitbox.x += velocityX;
        for(Map.Entry<Point, Wall> wall : panel.wallsByCords.entrySet()) {
            if(hitbox.intersects(wall.getValue().hitbox) && wall.getValue().isCollision) {
                hitbox.x -= velocityX;
                while(!wall.getValue().hitbox.intersects(hitbox)) {
                    hitbox.x += Math.signum(velocityX);
                }
                hitbox.x -= Math.signum(velocityX);
                velocityX = 0;
                x = hitbox.x;
            }
        }

        hitbox.y += velocityY;
        for(Map.Entry<Point, Wall> wall : panel.wallsByCords.entrySet()) {
            if(hitbox.intersects(wall.getValue().hitbox) && wall.getValue().isCollision) {
                hitbox.y -= velocityY;
                while(!wall.getValue().hitbox.intersects(hitbox)) {
                    hitbox.y += Math.signum(velocityY);
                }
                hitbox.y -= Math.signum(velocityY);
                velocityY = 0;
                y = hitbox.y;
            }
        }

        x += velocityX;
        y += velocityY;

        hitbox.x = x;
        hitbox.y = y;

        itemBar.x = x - startingPosX + 10;
        itemBar.y = y - startingPosY + 10;
    }

    public void draw(Graphics2D g2d) {
        //g2d.setColor(Color.BLACK);
        //g2d.fillRect(x, y, width, height);
        g2d.drawImage(image, x, y, panel);
        itemBar.draw(g2d);
    }
}
