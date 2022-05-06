package prj;

import org.joml.Vector2i;
import prj.log.Logger;
import prj.net.ClientNetworkManager;
import prj.net.packet.player.PlayerMovePacket;
import prj.world.Camera;
import prj.world.Direction;
import prj.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ClientThread extends JPanel{
    public static ClientThread instance = null;

    private final Logger logger = new Logger("");
    private World world;
    //private Camera cam;
    private ClientNetworkManager networkManager;

    private InputMap im;
    private ActionMap am;

    private long totalFpsUpdateFrames;
    private double fpsUpdateDelay, totalFpsUpdateTime;
    private double fps, targetMillis;
    private boolean paused, running;

    private Font defaultFont;

    public ClientThread(int width, int height) {
        logger.setName("Client").dbg("init start");
        instance = this;

        setBackground(Color.white);
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();

        initValues();
        loadActions();

        logger.dbg("init end");
    }

    public void initValues(){
        im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        am = getActionMap();

        paused = false;
        running = true;

        fps = 60;
        fpsUpdateDelay = 200;
        totalFpsUpdateFrames = 0;
        totalFpsUpdateTime = 0;

        targetMillis = 1000 / fps;

        defaultFont = new Font("Arial", Font.PLAIN, 11);

        //TODO load world from save or generate new
        world = new World();
        networkManager = new ClientNetworkManager();

        /*cam = new Camera();
        addMouseListener(cam);
        addMouseMotionListener(cam);*/
    }

    public void loadActions(){
        im.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "exit");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK), "pause");

        im.put(KeyStroke.getKeyStroke("pressed W"), "pw");
        im.put(KeyStroke.getKeyStroke("released W"), "rw");
        im.put(KeyStroke.getKeyStroke("pressed S"), "ps");
        im.put(KeyStroke.getKeyStroke("released S"), "rs");
        im.put(KeyStroke.getKeyStroke("pressed A"), "pa");
        im.put(KeyStroke.getKeyStroke("released A"), "ra");
        im.put(KeyStroke.getKeyStroke("pressed D"), "pd");
        im.put(KeyStroke.getKeyStroke("released D"), "rd");

        am.put("exit", new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                shutdown();
            }
        });
        am.put("pause", new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                paused ^= true;
            }
        });

        am.put("pw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(Direction.UP, true));
                world.getState().getPlayer().setMoving(Direction.UP, true);
            }
        });
        am.put("rw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(Direction.UP, false));
                world.getState().getPlayer().setMoving(Direction.UP, false);
            }
        });
        am.put("ps", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(Direction.DOWN, true));
                world.getState().getPlayer().setMoving(Direction.DOWN, true);
            }
        });
        am.put("rs", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(Direction.DOWN, false));
                world.getState().getPlayer().setMoving(Direction.DOWN, false);
            }
        });
        am.put("pa", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(Direction.LEFT, true));
                world.getState().getPlayer().setMoving(Direction.LEFT, true);
            }
        });
        am.put("ra", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(Direction.LEFT, false));
                world.getState().getPlayer().setMoving(Direction.LEFT, false);
            }
        });
        am.put("pd", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(Direction.RIGHT, true));
                world.getState().getPlayer().setMoving(Direction.RIGHT, true);
            }
        });
        am.put("rd", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(Direction.RIGHT, false));
                world.getState().getPlayer().setMoving(Direction.RIGHT, false);
            }
        });
    }

    public void update(double dt){
        totalFpsUpdateTime += dt;
        totalFpsUpdateFrames++;

        if(paused) return;

        if(totalFpsUpdateTime*1000 >= fpsUpdateDelay){
            fps = totalFpsUpdateFrames / totalFpsUpdateTime;
            totalFpsUpdateFrames = 0;
            totalFpsUpdateTime = 0;
        }

        world.updateState(dt);
    }

    public void draw(Graphics2D g){
        g.setColor(Color.black);
        g.setFont(defaultFont);

        g.drawString(String.format("FPS: %.0f", fps), 2, 11);
        /*Vector2i off = cam.getGlobalOffset();
        g.translate(off.x, off.y);*/

        world.draw(g);
    }

    public void run() {
        logger.out("thread start");

        networkManager.startServerInstance();

        long frameStart, lastFrameUpdate = System.nanoTime(), threadWait;

        while(running){
            frameStart = System.nanoTime();

            update((double)(frameStart - lastFrameUpdate) / 1000000000);
            repaint();

            lastFrameUpdate = frameStart;

            threadWait = (long)(targetMillis - (System.nanoTime() - frameStart) / 1000000);

            if(threadWait < 0){
                logger.warn(String.format("lag %dms ( %.2f frames at %.0f FPS )", -threadWait, -threadWait / targetMillis, 1000 / targetMillis));
                threadWait = 0;
            }

            try{
                Thread.sleep(threadWait);
            }catch (InterruptedException e){
                logger.err("interrupted: " + e.getMessage());
            }
        }

        networkManager.shutdown();
        logger.out("thread stop");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw((Graphics2D) g);
    }

    public void shutdown(){
        logger.dbg("shutdown");
        running = false;
    }

    public World getWorld() {
        return world;
    }
}
