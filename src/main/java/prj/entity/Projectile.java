package prj.entity;

import prj.ImgLoader;
import prj.RotatedIcon;
import prj.wall.Wall;
import prj.world.WorldState;

import java.awt.*;

public class Projectile {

    private int x;
    private int y;
    private int startingX;
    private int startingY;
    private int finalX;
    private int finalY;
    private double t;
    private double projectileSpeed;

    public Projectile(int startingX, int startingY, int finalX, int finalY, double projectileSpeed) {
        this.startingX = startingX;
        this.startingY = startingY;
        this.finalX = finalX;
        this.finalY = finalY;
        this.projectileSpeed = projectileSpeed;
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

    public int getStartingX() {
        return startingX;
    }

    public void setStartingX(int startingX) {
        this.startingX = startingX;
    }

    public int getStartingY() {
        return startingY;
    }

    public void setStartingY(int startingY) {
        this.startingY = startingY;
    }

    public int getFinalX() {
        return finalX;
    }

    public void setFinalX(int finalX) {
        this.finalX = finalX;
    }

    public int getFinalY() {
        return finalY;
    }

    public void setFinalY(int finalY) {
        this.finalY = finalY;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }

    public void setProjectileSpeed(double projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public void calculateCurrentPosition() {
        x = (int)(startingX + (finalX - startingX) * t);
        y = (int)(startingY + (finalY - startingY) * t);
    }

    public Point getProjectileGridCoords(){
        int cellCordsX, cellCordsY;
        if(x >= 0) {
            cellCordsX = x - x % 50;
        }
        else {
            cellCordsX = x - (50 + x % 50);
        }

        if(y >= 0) {
            cellCordsY = y - y % 50;
        }
        else {
            cellCordsY = y - (50 + y % 50);
        }

        return new Point(cellCordsX, cellCordsY);
    }

    public void moveProjectile(WorldState state) {
        calculateCurrentPosition();
        t += projectileSpeed;

        for(int i = getProjectileGridCoords().x - 100 ; i <= getProjectileGridCoords().x + 150 ; i += 50) {
            for (int j = getProjectileGridCoords().y - 100; j <= getProjectileGridCoords().y + 150; j += 50) {
                Wall wall = state.wallsByCords.get(new Point(i, j));
                if (wall != null) {

                }
            }
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x, y, 10, 10);
        g2d.fillOval(x, y, 10, 10);
    }
}
