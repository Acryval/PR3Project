package com.company;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        GamePanel panel = new GamePanel();
        this.add(panel);
        addKeyListener(new KeyChecker(panel));
        addMouseListener(new MouseChecker(panel));

        this.setTitle("Ruch - test");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
