package prj.entity;

import prj.ImgLoader;
import prj.item.ItemBar;
import prj.wall.Wall;
import prj.world.WorldState;

import java.awt.*;
import java.io.Serializable;
import java.util.Map;

public class Player implements Serializable {
    private int x;
    private int y;
    private int startingPosX;
    private int startingPosY;
    private int width;
    private int height;
    private double velocityX;
    private double velocityY;
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
        this.startingPosX = x;
        this.startingPosY = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.width = 50;
        this.height = 100;
        this.hitbox = new Rectangle(this.x, this.y, this.width, this.height);
        this.image = "player";
        this.itemBar = itemBar;
        this.loggedIn = false;
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

    public int getStartingPosX() {
        return startingPosX;
    }

    public void setStartingPosX(int startingPosX) {
        this.startingPosX = startingPosX;
    }

    public int getStartingPosY() {
        return startingPosY;
    }

    public void setStartingPosY(int startingPosY) {
        this.startingPosY = startingPosY;
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
    /* ****** */

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
            for(Map.Entry<Point, Wall> wall : state.getWallsByCords().entrySet()) {
                if(wall.getValue().getHitbox().intersects(hitbox) && wall.getValue().isCollision()) {
                    velocityY = -6;
                }
            }
            hitbox.y--;
        }
        velocityY += 0.3;

        hitbox.x += velocityX;
        for(Map.Entry<Point, Wall> wall : state.getWallsByCords().entrySet()) {
            if(hitbox.intersects(wall.getValue().getHitbox()) && wall.getValue().isCollision()) {
                hitbox.x -= velocityX;
                while(!wall.getValue().getHitbox().intersects(hitbox)) {
                    hitbox.x += Math.signum(velocityX);
                }
                hitbox.x -= Math.signum(velocityX);
                velocityX = 0;
                x = hitbox.x;
            }
        }

        hitbox.y += velocityY;
        for(Map.Entry<Point, Wall> wall : state.getWallsByCords().entrySet()) {
            if(hitbox.intersects(wall.getValue().getHitbox()) && wall.getValue().isCollision()) {
                hitbox.y -= velocityY;
                while(!wall.getValue().getHitbox().intersects(hitbox)) {
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

        /*
        itemBar.x = x - startingPosX + 10;
        itemBar.y = y - startingPosY + 10;
        itemBar.playerPosX = x;
        itemBar.playerPosY = y;
        if(keyRight) {
            itemBar.isFacingRight = true;
        }
        else if(keyLeft){
            itemBar.isFacingRight = false;
        }
        */

        /* kursor wciaz do poprawienia! */
        /*panel.getMouseCursor().setX(panel.getMouseCursor().getX()+ (int)velocityX);
        panel.getMouseCursor().setY(panel.getMouseCursor().getY() + (int)velocityY);*/

        itemBar.setX(x - startingPosX + 10);
        itemBar.setY(y - startingPosY + 10);
        itemBar.setPlayerPosX(x);
        itemBar.setPlayerPosY(y);
        if(keyRight) {
            itemBar.setFacingRight(true);
        }
        else if(keyLeft){
            itemBar.setFacingRight(false);
        }
    }

    public void draw(Graphics2D g2d) {
        if(!loggedIn) return;
        //g2d.setColor(Color.BLACK);
        //g2d.fillRect(x, y, width, height);
        g2d.drawImage(ImgLoader.get(image), x, y, null);
    }
}
