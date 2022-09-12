package prj;

import prj.gamestates.GameStateManager;
import prj.gamestates.MenuState;
import prj.net.packet.gamestate.ScreenDimensionPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class Prj extends JFrame{
    public static Prj instance;
    public static boolean LOG_PACKETS = false;
    public static boolean LOG_INFO = false;
    public static boolean DEBUG = false;
    public static boolean SHOWFPS = false;
    public static boolean DBACCESS = false;
    public static String LOGFILE = null;
    private final GameStateManager gsm;

    public Prj(String title, int width, int height) throws HeadlessException {
        instance = this;

        setupLogfile("GameLog");

        gsm = new GameStateManager(width, height);
        gsm.registerGameState("client", ClientThread.class);
        gsm.registerGameState("menu", MenuState.class);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(gsm);
        setTitle(title);

        pack();

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gsm.stop();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        gsm.setState("menu", new ScreenDimensionPacket(width, height));
        gsm.run();
    }

    public static void setupLogfile(String filename){
        if(LOG_INFO) {
            LOGFILE = "logs/" + filename + " " + new SimpleDateFormat("yyyy-MM-dd HH-mm").format(new Date()) + ".log";

            try {
                new FileWriter(LOGFILE).append("LOG Start @ ").append(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(ZonedDateTime.now())).append(System.lineSeparator()).append(System.lineSeparator()).close();
            } catch (IOException e) {
                System.err.println("File " + LOGFILE + " inaccessible");
                LOG_INFO = false;
                LOGFILE = null;
            }
        }
    }

    public static void main(String[] args) {
        for(String arg : args){
            if(arg.contains("--log")){
                LOG_INFO = true;
                if(arg.contains("packet")){
                    LOG_PACKETS = true;
                }
            }
            if(arg.contains("--debug")){
                Prj.DEBUG = true;
                if(arg.contains("packet")){
                    LOG_PACKETS = true;
                }
            }
            if(arg.contains("--showFps")){
                Prj.SHOWFPS = true;
            }
            if(arg.contains("--dba")){
                Prj.DBACCESS = true;
            }
        }

        new Prj("PR3 Project Window", 1200, 700);

        System.exit(0);
    }
}
