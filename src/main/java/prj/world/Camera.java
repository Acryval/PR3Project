package prj.world;

import org.joml.RoundingMode;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.joml.Vector2i;
import prj.ClientThread;
import prj.entity.Player;
import prj.item.Block;
import prj.item.Item;
import prj.item.Pickaxe;
import prj.wall.DefaultBreakableWall;
import prj.wall.DefaultTransparentWall;
import prj.wall.Wall;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class Camera implements MouseListener, MouseMotionListener {
    private final Vector2d pos;
    private final Vector2i offset, mouse, dm, scrPos;
    private boolean freeCam, mousePressed, isInRange;

    public Camera() {
        this.pos = new Vector2d(-ClientThread.instance.getWidth()/2.0, ClientThread.instance.getHeight()/2.0);
        this.offset = new Vector2i();
        this.mouse = new Vector2i();
        this.dm = new Vector2i();
        this.scrPos = new Vector2i();
        this.mousePressed = false;
        this.freeCam = true;
        this.isInRange = false;
    }

    public void setInRange(boolean inRange) {
        isInRange = inRange;
    }

    public Vector2i getGlobalOffset(){
        return new Vector2i((int)pos.x, (int)pos.y).add(offset);
    }

    private void updateMouse(){
        mouse.set(scrPos).sub(getGlobalOffset());
    }

    public Vector2i getMouse() {
        return mouse;
    }

    public Vector2i getOffset() {
        return offset;
    }

    public Vector2d getPos() {
        return pos;
    }

    public void attach(){
        freeCam = false;
    }

    public void detach(){
        freeCam = true;
    }

    public void update(WorldState state, Player player){
        if(player == null) return;
        this.pos.set(-player.getX(), -player.getY());
        updateMouse();
        setInRange(false);

        Item itemHeld = player.getItemBar().getHeldItem();
        if(itemHeld == null){
            return;
        }

        Wall pointedWall = state.wallsByCords.get(getGridCoords());
        if(pointedWall == null) {
            return;
        }

        setInRange(offset.length() <= itemHeld.getRange() && pointedWall.isBreakable());
    }

    public Point getGridCoords(){
        int cursorOnMapX, cursorOnMapY;
        Vector2i scr = ClientThread.instance.scrSize;

        cursorOnMapX = mouse.x - scr.x/2;
        cursorOnMapY = mouse.y - scr.y/2;

        int cellCordsX, cellCordsY;
        if(cursorOnMapX >= 0) {
            cellCordsX = cursorOnMapX - cursorOnMapX % 50;
        }
        else {
            cellCordsX = cursorOnMapX - (50 + cursorOnMapX % 50);
        }

        if(cursorOnMapY >= 0) {
            cellCordsY = cursorOnMapY - cursorOnMapY % 50;
        }
        else {
            cellCordsY = cursorOnMapY - (50 + cursorOnMapY % 50);
        }

        return new Point(cellCordsX, cellCordsY);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mousePressed = true;

        World world = ClientThread.instance.getWorld();
        Item itemHeld = world.getLocalPlayer().getItemBar().getHeldItem();
        if(itemHeld == null) return;

        Point cellCords = getGridCoords();
        int cellCordsX = cellCords.x;
        int cellCordsY = cellCords.y;

        double cursorToPlayerDistance = getOffset().length();

        synchronized (world.getState()) {
            if (itemHeld instanceof Pickaxe pickaxe) {
                if (world.getState().wallsByCords.get(cellCords) == null) return;
                if (world.getState().wallsByCords.get(cellCords).isBreakable() && cursorToPlayerDistance <= pickaxe.getRange()) {
                    world.getState().wallsByCords.get(cellCords).setDurability(world.getState().wallsByCords.get(cellCords).getDurability() - 10);
                    if (world.getState().wallsByCords.get(cellCords).getDurability() <= 0) {
                        world.getState().wallsByCords.put(new Point(cellCordsX, cellCordsY), new DefaultTransparentWall(cellCordsX, cellCordsY));
                    }
                }
            } else if (itemHeld instanceof Block block) {
                boolean isBlockNeighbour = false;

                Wall w = world.getState().wallsByCords.get(new Point(cellCordsX - 50, cellCordsY));
                if (w != null && w.isCollision()) isBlockNeighbour = true;
                else w = world.getState().wallsByCords.get(new Point(cellCordsX + 50, cellCordsY));
                if (!isBlockNeighbour && w != null && w.isCollision()) isBlockNeighbour = true;
                else w = world.getState().wallsByCords.get(new Point(cellCordsX, cellCordsY - 50));
                if (!isBlockNeighbour && w != null && w.isCollision()) isBlockNeighbour = true;
                else w = world.getState().wallsByCords.get(new Point(cellCordsX, cellCordsY + 50));
                if (!isBlockNeighbour && w != null && w.isCollision()) isBlockNeighbour = true;

                boolean isPlayerCollision = world.getLocalPlayer().getHitbox().intersects(new Rectangle(cellCordsX, cellCordsY, 50, 50));

                if (isBlockNeighbour && !isPlayerCollision && cursorToPlayerDistance <= block.getRange()) {
                    world.getState().wallsByCords.put(cellCords, new DefaultBreakableWall(cellCordsX, cellCordsY));
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if(freeCam) {
            Vector2i temp = new Vector2i(mouseEvent.getX(), mouseEvent.getY());
            if (mousePressed) {
                mousePressed = false;
                dm.set(temp);
            } else {
                offset.add(temp).sub(dm);
                dm.set(temp);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        scrPos.set(mouseEvent.getX(), mouseEvent.getY());

        if(!freeCam) {
            offset.set(scrPos).sub(new Vector2i(ClientThread.instance.scrSize).div(2)).div(-5);
        }

        updateMouse();
    }

    public void draw(Graphics2D g){
        Vector2i scr = ClientThread.instance.scrSize;
        g.setColor(Color.BLACK);
        g.fillOval(mouse.x - 20 / 2 - scr.x/2, mouse.y - 20 / 2 - scr.y/2, 20 + 2, 20 + 2);
        if(isInRange) {
            g.setColor(Color.GREEN);
        }
        else {
            g.setColor(Color.RED);
        }
        g.fillOval(mouse.x + 1 - 20 / 2 - scr.x/2, mouse.y + 1 - 20 / 2 - scr.y/2, 20, 20);
    }
}
