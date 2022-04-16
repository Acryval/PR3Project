package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.Timer;

public class GamePanel extends JPanel implements ActionListener {

    private Player player;
    //ArrayList<Wall> walls = new ArrayList<>();
    HashMap<Point, Wall> wallsByCords = new HashMap<>();
    Timer timer;

    public GamePanel() {
        this.setPreferredSize(new Dimension(1400, 900));
        this.setBackground(Color.LIGHT_GRAY);
        this.setVisible(true);

        player = new Player((1400 - 50)/ 2, (900 - 100)/ 2, this);
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
        for(int i = -500 ; i < 1300 ; i += 50) {
            for(int j = -500 ; j < 1100 ; j += 50) {
                wallsByCords.put(new Point(i, j), new Wall(i, j, 50, 50, 100, false, false, this));
            }
        }
        for(int i = -50 ; i < 800 ; i += 50) {
            wallsByCords.put(new Point(i, 600), new Wall(i, 600, 50, 50, 100, true, true, this));
        }
        wallsByCords.put(new Point(0, 550), new Wall(0, 550, 50, 50, 100, true, true, this));
        wallsByCords.put(new Point(-50, 500), new Wall(-50, 500, 50, 50, 100, true, true, this));
        wallsByCords.put(new Point(-100, 450), new Wall(-100, 450, 50, 50, 100, true, true, this));
        wallsByCords.put(new Point(-150, 400), new Wall(-150, 400, 50, 50, 100, true, true, this));

        wallsByCords.put(new Point(200, 550), new Wall(200, 550, 50, 50, 100, true, true, this));
        wallsByCords.put(new Point(250, 550), new Wall(250, 550, 50, 50, 100, true, true, this));
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(player.startingPosX - player.x, player.startingPosY - player.y);
        player.draw(g2d);
        for(Map.Entry<Point, Wall> wall : wallsByCords.entrySet()) {
            wall.getValue().draw(g2d);
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
        if(e.getKeyChar() == ' ') {
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
        if(e.getKeyChar() == ' ') {
            player.keyUp = false;
        }
    }

    public void mousePressed(MouseEvent e) {
        int cursorOnMapX, cursorOnMapY;

        cursorOnMapX = (e.getX() - 8) + player.x  - player.startingPosX;
        cursorOnMapY = (e.getY() - 31) + player.y  - player.startingPosY;

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

        /* DEBUG
        System.out.println("(x:" + e.getX() + " y:" + e.getY() + " cx:" + cellCordsX + " cy:" + cellCordsY + "), " +
                "(px:" + player.x + " py:" + player.y + ")");
        System.out.println("player: (" + player.x  + "," + player.y + ")" + " cursor: (" + (e.getX() - 8) + "," + (e.getY() - 31) + ")");
        */
        //System.out.println("[" + cursorOnMapX + "," + cursorOnMapY + "]");
        //System.out.println("{" + cellCordsX + "," + cellCordsY + "}");
        Point cellCords = new Point(cellCordsX, cellCordsY);

        if(wallsByCords.get(cellCords).isBreakable) {
            System.out.println("{" + cellCordsX + "," + cellCordsY + " durability: " + wallsByCords.get(cellCords).durability + "}");
            wallsByCords.get(cellCords).durability -= 10;
            if(wallsByCords.get(cellCords).durability <= 0) {
                wallsByCords.put(new Point(cellCordsX, cellCordsY), new Wall(cellCordsX, cellCordsY, 50, 50, 100, false, false, this));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

    }
}
