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
    private MouseCursor mouseCursor;
    private HashMap<Point, Wall> wallsByCords = new HashMap<>();
    private Timer timer;

    public GamePanel() {
        this.setPreferredSize(new Dimension(1400, 900));
        this.setBackground(Color.LIGHT_GRAY);
        this.setVisible(true);

        ItemBar itemBar = new ItemBar(10, 10, (1400 - 50)/ 2, (900 - 100)/ 2, 10, 50, 50, 10, 0);
        player = new Player((1400 - 50)/ 2, (900 - 100)/ 2, this, itemBar);
        mouseCursor = new MouseCursor(player.getX(), player.getY(), true);
        player.getItemBar().addItem(new Item("pickaxe", "Pickaxe.png", "PickaxeIcon.png"));
        player.getItemBar().addItem(new Item("none", "none.png", "noneIcon.png"));
        player.getItemBar().addItem(new Item("none", "none.png", "noneIcon.png"));
        player.getItemBar().addItem(new Item("none", "none.png", "noneIcon.png"));
        player.getItemBar().addItem(new Item("none", "none.png", "noneIcon.png"));
        player.getItemBar().addItem(new Item("none", "none.png", "noneIcon.png"));
        player.getItemBar().addItem(new Item("none", "none.png", "noneIcon.png"));
        player.getItemBar().addItem(new Item("none", "none.png", "noneIcon.png"));
        player.getItemBar().addItem(new Item("none", "none.png", "noneIcon.png"));
        player.getItemBar().addItem(new Item("none", "none.png", "noneIcon.png"));
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

    /* Settery i gettery */
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public MouseCursor getMouseCursor() {
        return mouseCursor;
    }

    public void setMouseCursor(MouseCursor mouseCursor) {
        this.mouseCursor = mouseCursor;
    }

    public HashMap<Point, Wall> getWallsByCords() {
        return wallsByCords;
    }

    public void setWallsByCords(HashMap<Point, Wall> wallsByCords) {
        this.wallsByCords = wallsByCords;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
    /* ****** */

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
        g2d.translate(player.getStartingPosX() - player.getX(), player.getStartingPosY() - player.getY());
        player.draw(g2d);
        for(Map.Entry<Point, Wall> wall : wallsByCords.entrySet()) {
            wall.getValue().draw(g2d);
        }

        //mouseCursor.setX(mouseCursor.getX() + (int)player.getVelocityX());
        //mouseCursor.setY(mouseCursor.getY() + (int)player.getVelocityY());
        int cellCordsX, cellCordsY;
        if(mouseCursor.getX() >= 0) {
            cellCordsX = mouseCursor.getX() - mouseCursor.getX() % 50;
        }
        else {
            cellCordsX = mouseCursor.getX() - (50 + mouseCursor.getX() % 50);
        }

        if(mouseCursor.getY() >= 0) {
            cellCordsY = mouseCursor.getY() - mouseCursor.getY() % 50;
        }
        else {
            cellCordsY = mouseCursor.getY() - (50 + mouseCursor.getY() % 50);
        }

        Point cellCords = new Point(cellCordsX, cellCordsY);
        if(Math.sqrt((mouseCursor.getX() - player.getX()) * (mouseCursor.getX() - player.getX()) + (mouseCursor.getY() - player.getY()) * (mouseCursor.getY() - player.getY())) <= 300
                && wallsByCords.get(cellCords).isBreakable()) {
            mouseCursor.setInRange(true);
        }
        else {
            mouseCursor.setInRange(false);
        }

        mouseCursor.draw(g2d);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == 'a') {
            player.setKeyLeft(true);
        }
        if(e.getKeyChar() == 'd') {
            player.setKeyRight(true);
        }
        if(e.getKeyChar() == ' ') {
            player.setKeyUp(true);
        }

        if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
            if(e.getKeyChar() == '0') {
                player.getItemBar().setInHandItemIndex(9);
            }
            else {
                player.getItemBar().setInHandItemIndex(e.getKeyChar() - 48 - 1);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyChar() == 'a') {
            player.setKeyLeft(false);
        }
        if(e.getKeyChar() == 'd') {
            player.setKeyRight(false);
        }
        if(e.getKeyChar() == ' ') {
            player.setKeyUp(false);
        }
    }

    public void mousePressed(MouseEvent e) {
        int cursorOnMapX, cursorOnMapY;

        cursorOnMapX = (e.getX() - 8) + player.getX() - player.getStartingPosX();
        cursorOnMapY = (e.getY() - 31) + player.getY() - player.getStartingPosY();

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

        if(wallsByCords.get(cellCords).isBreakable()
                && Math.sqrt((cursorOnMapX - player.getX()) * (cursorOnMapX - player.getX()) + (cursorOnMapY - player.getY()) * (cursorOnMapY - player.getY())) <= 300
                && player.getItemBar().getInHandItemType() == "pickaxe") {

            //System.out.println("{" + cellCordsX + "," + cellCordsY + " durability: " + wallsByCords.get(cellCords).getDurability() + "}");
            wallsByCords.get(cellCords).setDurability(wallsByCords.get(cellCords).getDurability() - 10);
            if(wallsByCords.get(cellCords).getDurability() <= 0) {
                wallsByCords.put(new Point(cellCordsX, cellCordsY), new Wall(cellCordsX, cellCordsY, 50, 50, 100, false, false, this));
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        int cursorOnMapX, cursorOnMapY;

        cursorOnMapX = (e.getX() - 8) + player.getX()  - player.getStartingPosX();
        cursorOnMapY = (e.getY() - 31) + player.getY()  - player.getStartingPosY();

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
        Point cellCords = new Point(cellCordsX, cellCordsY);
        mouseCursor.setX(cursorOnMapX);
        mouseCursor.setY(cursorOnMapY);

        if(Math.sqrt((cursorOnMapX - player.getX()) * (cursorOnMapX - player.getX()) + (cursorOnMapY - player.getY()) * (cursorOnMapY - player.getY())) <= 300
        && wallsByCords.get(cellCords).isBreakable()) {
            mouseCursor.setInRange(true);
        }
        else {
            mouseCursor.setInRange(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

    }
}
