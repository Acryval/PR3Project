package prj.wall;

import prj.ImgLoader;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Wall implements Serializable {

    private int x;
    private int y;
    private int width;
    private int height;
    private int durability;
    private int damage;
    private boolean isBreakable;
    private boolean isCollision;
    private boolean isDamaging;
    private Rectangle hitbox;
    private ArrayList<String> image = new ArrayList<>();
    private String type;

    public Wall(int x, int y, int width, int height, boolean isBreakable, boolean isCollision, boolean isDamaging, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isBreakable = isBreakable;
        this.isCollision = isCollision;
        this.isDamaging = isDamaging;
        hitbox = new Rectangle(x, y, width, height);
        this.type = type;
        loadImage();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isDamaging() {
        return isDamaging;
    }

    public void setDamaging(boolean damaging) {
        isDamaging = damaging;
    }

    public boolean isBreakable() {
        return isBreakable;
    }

    public void setBreakable(boolean breakable) {
        isBreakable = breakable;
    }

    public boolean isCollision() {
        return isCollision;
    }

    public void setCollision(boolean collision) {
        isCollision = collision;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public Image getImage(int indx) {
        return ImgLoader.get(image.get(indx));
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public abstract void loadImage();
    public abstract void draw(Graphics2D g2d);
}
