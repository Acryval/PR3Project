package prj.entity;

import prj.ImgLoader;
import prj.RotatedIcon;
import prj.item.ItemBar;
import prj.wall.Wall;
import prj.world.WorldState;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Map;

public class Player implements Serializable {
    private int x;
    private int y;
    private int width;
    private int height;
    private double velocityX;
    private double velocityY;
    private int health;
    private Rectangle hitbox;
    private boolean keyLeft;
    private boolean keyRight;
    private boolean keyUp;
    private boolean loggedIn;
    private String image;
    private ItemBar itemBar;

    public Player(int x, int y, ItemBar itemBar) {
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.health = 100;
        this.width = 50;
        this.height = 100;
        this.hitbox = new Rectangle(this.x, this.y, this.width, this.height);
        this.image = "player";
        this.itemBar = itemBar;
        this.loggedIn = false;
    }

    /* Settery i gettery */

    public void setPos(int x, int y){
        setX(x);
        setY(y);
    }

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

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public boolean isKeyLeft() {
        return keyLeft;
    }

    public void setKeyLeft(boolean keyLeft) {
        this.keyLeft = keyLeft;
    }

    public boolean isKeyRight() {
        return keyRight;
    }

    public void setKeyRight(boolean keyRight) {
        this.keyRight = keyRight;
    }

    public boolean isKeyUp() {
        return keyUp;
    }

    public void setKeyUp(boolean keyUp) {
        this.keyUp = keyUp;
    }

    public Image getImage() {
        return ImgLoader.get(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ItemBar getItemBar() {
        return itemBar;
    }

    public void setItemBar(ItemBar itemBar) {
        this.itemBar = itemBar;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void update(WorldState state, double dt) {
        if(!loggedIn) return;

        if(keyLeft && keyRight || !keyLeft && !keyRight) {
            velocityX *= 0.9;
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
            for(Map.Entry<Point, Wall> wall : state.wallsByCords.entrySet()) {
                if(wall.getValue().getHitbox().intersects(hitbox) && wall.getValue().isCollision()) {
                    velocityY = -6;
                }
            }
            hitbox.y--;
        }
        velocityY += 0.3;

        hitbox.x += velocityX;
        for(Map.Entry<Point, Wall> wall : state.wallsByCords.entrySet()) {
            if(hitbox.intersects(wall.getValue().getHitbox()) && wall.getValue().isCollision()) {
                if(!wall.getValue().isDamaging()) {
                    hitbox.x -= velocityX;
                    while(!wall.getValue().getHitbox().intersects(hitbox)) {
                        hitbox.x += Math.signum(velocityX);
                    }
                    hitbox.x -= Math.signum(velocityX);
                    velocityX = 0;
                    x = hitbox.x;
                }
                else {
                    if(health > 0) {
                        System.out.println("Gracz otrzymal obrazenia od kolcow (X)");
                        setHealth(getHealth() - wall.getValue().getDamage());
                        hitbox.x -= velocityX;
                        while(!wall.getValue().getHitbox().intersects(hitbox)) {
                            hitbox.x += Math.signum(velocityX);
                        }
                        hitbox.x -= Math.signum(velocityX);
                        velocityX = -Math.signum(velocityX) * 10;
                        x = hitbox.x;
                    }
                    else {
                        /* gracz umiera */
                    }
                }
            }
        }

        hitbox.y += velocityY;
        for(Map.Entry<Point, Wall> wall : state.wallsByCords.entrySet()) {
            if(hitbox.intersects(wall.getValue().getHitbox()) && wall.getValue().isCollision()) {
                if(!wall.getValue().isDamaging()) {
                    hitbox.y -= velocityY;
                    while(!wall.getValue().getHitbox().intersects(hitbox)) {
                        hitbox.y += Math.signum(velocityY);
                    }
                    hitbox.y -= Math.signum(velocityY);
                    velocityY = 0;
                    y = hitbox.y;
                }
                else {
                    if(health > 0) {
                        System.out.println("Gracz otrzymal obrazenia od kolcow (Y)");
                        setHealth(getHealth() - wall.getValue().getDamage());
                        hitbox.y -= velocityY;
                        while(!wall.getValue().getHitbox().intersects(hitbox)) {
                            hitbox.y += Math.signum(velocityY);
                        }
                        hitbox.y -= Math.signum(velocityY);
                        velocityY = -Math.signum(velocityY) * 10;
                        y = hitbox.y;
                    }
                    else {
                        /* gracz umiera */
                    }
                }
            }
        }

        x += velocityX;
        y += velocityY;

        hitbox.x = x;
        hitbox.y = y;

        if(itemBar != null) {
            itemBar.setX(x - 665);
            itemBar.setY(y - 390);
            itemBar.setPlayerPosX(x);
            itemBar.setPlayerPosY(y);
            if (keyRight) {
                itemBar.setFacingRight(true);
            } else if (keyLeft) {
                itemBar.setFacingRight(false);
            }
        }
    }

    public void draw(Graphics2D g2d) {
        if(!loggedIn) return;
        if(health <= 0) {
            RotatedIcon ri = new RotatedIcon(new ImageIcon(ImgLoader.get(image)), 90.0) ;
            ri.paintIcon(null, g2d, x, y);
        }
        else {
            g2d.drawImage(ImgLoader.get(image), x, y, null);
        }
    }
}
