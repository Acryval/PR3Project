package prj.wall;

import javax.swing.*;
import java.awt.*;

public class DefaultBreakableWall extends Wall {

    public DefaultBreakableWall(int x, int y) {
        super(x, y, 50, 50, 100, true, true, "DefaultBreakableWall");
        super.setHitbox(new Rectangle(x, y, 50, 50));
        loadImage();
    }

    @Override
    public void loadImage() {
        super.getImage().add("BlockPrototype");
        super.getImage().add("BlockPrototypeBrokenStage1");
        super.getImage().add("BlockPrototypeBrokenStage2");
        super.getImage().add("BlockPrototypeBrokenStage3");
        super.getImage().add("BlockPrototypeBrokenStage4");
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (super.isCollision()) {
            /*
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, width, height);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(x + 1, y + 1, width - 1, height - 1);
            */
            if (this.getDurability() < 20) {
                g2d.drawImage(super.getImage(4), super.getX(), super.getY(), null);
            } else if (this.getDurability() < 40) {
                g2d.drawImage(super.getImage(3), super.getX(), super.getY(), null);
            } else if (this.getDurability() < 60) {
                g2d.drawImage(super.getImage(2), super.getX(), super.getY(), null);
            } else if (this.getDurability() < 80) {
                g2d.drawImage(super.getImage(1), super.getX(), super.getY(), null);
            } else if (this.getDurability() <= 100) {
                g2d.drawImage(super.getImage(0), super.getX(), super.getY(), null);
            }
        }
    }
}
