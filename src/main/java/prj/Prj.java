package prj;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Prj extends JFrame {
    public Prj(String title, int width, int height) throws HeadlessException, IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new ClientThread(width, height));
        setTitle(title);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        ((ClientThread)getContentPane()).run();
    }

    public static void main(String[] args) throws IOException {
        new Prj("PR3 Project Window", 1200, 700);
    }
}
