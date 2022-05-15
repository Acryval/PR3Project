package com.company;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseChecker extends MouseAdapter {

    private GamePanel panel;

    public MouseChecker(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        panel.mousePressed(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        panel.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        panel.mouseMoved(e);
    }
}
