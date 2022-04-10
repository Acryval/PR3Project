package prj;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class Prj extends JFrame {
    public static boolean DEBUG = true;
    public static String LOGFILE = null;

    public Prj(String title, int width, int height) throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new ClientThread(width, height));
        setTitle(title);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        setupLogfile("log");

        ((ClientThread)getContentPane()).run();
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
