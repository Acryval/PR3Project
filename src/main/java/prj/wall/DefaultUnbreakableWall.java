package prj.wall;

import java.awt.*;

public class DefaultUnbreakableWall extends Wall{

    public DefaultUnbreakableWall(int x, int y) {
        super(x, y, 50, 50, false, true, false, "DefaultUnbreakableWall");
    }

    @Override
    public void loadImage() {
        super.getImage().add("UnbreakablePrototype");
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(super.getImage(0), super.getX(), super.getY(), null);
    }
}
