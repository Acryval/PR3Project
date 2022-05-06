package prj.world;

import org.joml.RoundingMode;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.joml.Vector2i;
import prj.ClientThread;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class Camera implements MouseListener, MouseMotionListener {
    private final Vector2d pos;
    private final Vector2i offset, mouse, dm;
    private boolean freeCam, mousePressed;

    public Camera() {
        this.pos = new Vector2d(-ClientThread.instance.getWidth()/2.0, ClientThread.instance.getHeight()/2.0);
        this.offset = new Vector2i();
        this.mouse = new Vector2i();
        this.dm = new Vector2i();
        this.mousePressed = false;
        this.freeCam = true;
    }

    public Vector2i getGlobalOffset(){
        return new Vector2i((int)pos.x, (int)pos.y).add(offset);
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

    public void attachTo(Vector2d npos){
        setPos(npos);
        freeCam = false;
    }

    public void detach(){
        freeCam = true;
    }

    public void setPos(Vector2d pos){
        this.pos.set(-pos.x, pos.y);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mousePressed = true;
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
        Vector2i screenPos = new Vector2i(mouseEvent.getX(), mouseEvent.getY());

        if(!freeCam) {
            offset.set(screenPos).sub(new Vector2i(ClientThread.instance.scrSize).div(2)).div(-5);
        }

        mouse.set(screenPos).sub(getGlobalOffset());
    }
}
