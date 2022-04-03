package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel implements ActionListener {

    private Player player;
    ArrayList<Wall> walls = new ArrayList<>();
    Timer timer;

    public GamePanel() {
        this.setPreferredSize(new Dimension(700, 700));
        this.setBackground(Color.LIGHT_GRAY);
        //this.setFocusable(true); ???
        this.setVisible(true);

        player = new Player(400, 300, this);
        makeWalls();
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                player.move();
                repaint();
            }
        }, 0, 17);
    }

    public void makeWalls() {
        for(int i = 50 ; i < 650 ; i += 50) {
            walls.add(new Wall(i, 600, 50, 50));
        }
        walls.add(new Wall(400, 550, 50, 50));
        walls.add(new Wall(450, 500, 50, 50));
        walls.add(new Wall(500, 450, 50, 50));
        walls.add(new Wall(550, 400, 50, 50));

        walls.add(new Wall(300, 350, 50, 50));
        walls.add(new Wall(250, 350, 50, 50));
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        player.draw(g2d);
        for(Wall wall : walls) {
            wall.draw(g2d);
        }
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == 'a') {
            player.keyLeft = true;
        }
        if(e.getKeyChar() == 'd') {
            player.keyRight = true;
        }
        if(e.getKeyChar() == 's') {
            player.keyDown = true;
        }
        if(e.getKeyChar() == 'w') {
            player.keyUp = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyChar() == 'a') {
            player.keyLeft = false;
        }
        if(e.getKeyChar() == 'd') {
            player.keyRight = false;
        }
        if(e.getKeyChar() == 's') {
            player.keyDown = false;
        }
        if(e.getKeyChar() == 'w') {
            player.keyUp = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

    }
}