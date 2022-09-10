package prj;

import prj.entity.User;
import prj.gamestates.GameStateManager;
import prj.net.packet.gamestate.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class Prj extends JFrame{
    public static boolean LOG_PACKETS = false;
    public static boolean LOG_INFO = false;
    public static boolean DEBUG = false;
    public static boolean SHOWFPS = false;
    public static String LOGFILE = null;
    private final GameStateManager gsm;

    public Prj(String title, int width, int height) throws HeadlessException {
        setupLogfile("GameLog");

        gsm = new GameStateManager(width, height);
        gsm.registerGameState("client", ClientThread.class);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(gsm);
        setTitle(title);

        pack();

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

        gsm.setState("client", new SetUsernamePacket("Acw"), new ConnectToServerPacket(new InetSocketAddress("25.68.34.237", 53912)), new PassDataPacket(), new ScreenDimensionPacket(width, height));
        gsm.run();
    }

    public static void setupLogfile(String filename){
        if(LOG_INFO) {
            LOGFILE = "logs/" + filename + " " + new SimpleDateFormat("yyyy-MM-dd HH-mm").format(new Date()) + ".log";

            try {
                new FileWriter(LOGFILE).append("LOG Start @ ").append(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(ZonedDateTime.now())).append(System.lineSeparator()).append(System.lineSeparator()).close();
            } catch (IOException e) {
                System.err.println("File " + LOGFILE + " inaccessible");
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
        }

        new Prj("PR3 Project Window", 1200, 700);

        System.exit(0);
    }
}
