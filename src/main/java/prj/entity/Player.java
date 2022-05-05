package prj.entity;

import org.joml.Vector2d;
import prj.net.packet.Packable;
import prj.net.packet.PacketElement;
import prj.world.Direction;

import java.awt.*;

public class Player implements Packable {
    public static final double MAX_VEL_DEFAULT = 100.0D;

    private boolean moveUp, moveDown, moveLeft, moveRight;

    @PacketElement
    private final Vector2d pos;
    @PacketElement
    private double maxVelocity;

    public Player(Vector2d pos) {
        this.pos = pos;
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
        maxVelocity = MAX_VEL_DEFAULT;
    }

    public Player(){
        this(new Vector2d());
    }

    public Vector2d getPos() {
        return pos;
    }

    public void setPos(Vector2d pos) {
        this.pos.set(pos);
    }

    public void setMoving(Direction dir, boolean value){
        switch(dir){
            case UP -> moveUp = value;
            case DOWN -> moveDown = value;
            case LEFT -> moveLeft = value;
            case RIGHT -> moveRight = value;
        }
    }

    public void update(double dt){
        Vector2d move = new Vector2d();

        if(moveUp){
            move.add(new Vector2d(0, 1));
        }

        if(moveDown){
            move.add(new Vector2d(0, -1));
        }

        if(moveRight){
            move.add(new Vector2d(1, 0));
        }

        if(moveLeft){
            move.add(new Vector2d(-1, 0));
        }

        pos.add(move.normalize().mul(maxVelocity*dt));
    }

    public void draw(Graphics2D g){
        g.setColor(Color.BLACK);
        g.fillRect((int)pos.x, -(int)pos.y, 10, 10);
    }
}
