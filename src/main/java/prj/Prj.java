package prj;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class Prj extends JFrame{
    public static boolean DEBUG = true;
    public static String LOGFILE = null;

    private final ClientThread cth;

    public Prj(String title, int width, int height) throws HeadlessException {
        setupLogfile("GameLog");

        cth = new ClientThread(width, height);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(cth);
        setTitle(title);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cth.shutdown();
            }
        });
        cth.run();
    }

    public static void setupLogfile(String filename){
        LOGFILE = "logs/" + filename + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + ".log";

        try {
            new FileWriter(LOGFILE).append("LOG Start @ ").append(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(ZonedDateTime.now())).append(System.lineSeparator()).append(System.lineSeparator()).close();
        }catch (IOException e){
            System.err.println("File " + LOGFILE + " inaccessible");
        }
    }

    public static void main(String[] args) {
        new Prj("PR3 Project Window", 1200, 700);
    }
}
