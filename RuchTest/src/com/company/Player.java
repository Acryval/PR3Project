package com.company;

import java.awt.*;

public class Player {

    GamePanel panel;
    private int x;
    private int y;
    private int width;
    private int height;
    private double velocityX;
    private double velocityY;
    Rectangle hitbox;
    public boolean keyLeft;
    public boolean keyRight;
    public boolean keyDown;
    public boolean keyUp;

    public Player(int x, int y, GamePanel panel) {
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.panel = panel;
        this.width = 50;
        this.height = 100;
        hitbox = new Rectangle(this.x, this.y, this.width, this.height);
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
            for(Wall wall : panel.walls) {
                if(wall.hitbox.intersects(hitbox)) {
                    velocityY = -6;
                }
            }
            hitbox.y--;
        }
        velocityY += 0.3;

        hitbox.x += velocityX;
        for(Wall wall : panel.walls) {
            if(hitbox.intersects(wall.hitbox)) {
                hitbox.x -= velocityX;
                while(!wall.hitbox.intersects(hitbox)) {
                    hitbox.x += Math.signum(velocityX);
                }
                hitbox.x -= Math.signum(velocityX);
                velocityX = 0;
                x = hitbox.x;
            }
        }

        hitbox.y += velocityY;
        for(Wall wall : panel.walls) {
            if(hitbox.intersects(wall.hitbox)) {
                hitbox.y -= velocityY;
                while(!wall.hitbox.intersects(hitbox)) {
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
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x, y, width, height);
    }
}
