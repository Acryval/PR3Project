package prj.wall;

import java.awt.*;

public class DefaultTransparentWall extends Wall{

    public DefaultTransparentWall(int x, int y) {
        super(x, y, 50, 50, 0, false, false);
        super.setHitbox(new Rectangle(x, y, 50, 50));
    }

    @Override
    public void loadImage() {

    }
    @Override
    public void draw(Graphics2D g2d) {

    }
}
