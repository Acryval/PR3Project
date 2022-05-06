package prj.world;

import org.joml.Vector2i;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Camera implements MouseListener, MouseMotionListener {
    private Vector2i pos;
    private final Vector2i offset, mouse, dm;
    private boolean freeCam, mousePressed;

    public Camera() {
        this.pos = new Vector2i();
        this.offset = new Vector2i();
        this.mouse = new Vector2i();
        this.dm = new Vector2i();
        this.mousePressed = false;
        this.freeCam = true;
    }

    public Vector2i getGlobalOffset(){
        return new Vector2i(pos).add(offset);
    }

    public Vector2i getMouse() {
        return mouse;
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
            offset.set(screenPos).div(2);
        }

        mouse.set(screenPos).sub(getGlobalOffset());
    }
}
