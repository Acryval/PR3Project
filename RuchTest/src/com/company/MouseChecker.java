package com.company;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseChecker extends MouseAdapter {

    GamePanel panel;
    int x;
    int y;

    public MouseChecker(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        panel.mousePressed(e);
    }
}
