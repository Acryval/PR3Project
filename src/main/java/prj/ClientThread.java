package prj;

import org.joml.Vector2i;
import prj.log.Logger;
import prj.net.ClientNetworkManager;
import prj.net.packet.player.PlayerMovePacket;
import prj.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;

import static prj.net.packet.player.PlayerMovePacket.Direction.*;

public class ClientThread extends JPanel implements MouseListener, MouseMotionListener {
    private final Logger logger = new Logger("");
    private World world;
    private ClientNetworkManager networkManager;

    private InputMap im;
    private ActionMap am;

    private long totalFpsUpdateFrames;
    private double fpsUpdateDelay, totalFpsUpdateTime;
    private double fps, targetMillis;
    private boolean paused, mousePressed, running;
    private Vector2i mouse, dm, offset;

    private Font defaultFont;

    public ClientThread(int width, int height) {
        logger.setName("Client").dbg("init start");
        setBackground(Color.white);
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();

        addMouseListener(this);
        addMouseMotionListener(this);

        initValues();
        loadActions();
        logger.dbg("init end");
    }

    public void initValues(){
        im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        am = getActionMap();

        paused = false;
        mousePressed = false;
        running = true;

        fps = 60;
        fpsUpdateDelay = 200;
        totalFpsUpdateFrames = 0;
        totalFpsUpdateTime = 0;

        targetMillis = 1000 / fps;

        mouse = new Vector2i();
        dm = new Vector2i();
        offset = new Vector2i();

        defaultFont = new Font("Arial", Font.PLAIN, 11);

        //TODO load world from save or generate new
        world = new World(0, 10, this);
        if(world.getConnectionListener().doesListenerSocketFailed()) System.exit(1);
        networkManager = new ClientNetworkManager(this);
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
                networkManager.send(new PlayerMovePacket(UP, true));
            }
        });
        am.put("rw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(UP, false));
            }
        });
        am.put("ps", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(DOWN, true));
            }
        });
        am.put("rs", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(DOWN, false));
            }
        });
        am.put("pa", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(LEFT, true));
            }
        });
        am.put("ra", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(LEFT, false));
            }
        });
        am.put("pd", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(RIGHT, true));
            }
        });
        am.put("rd", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                networkManager.send(new PlayerMovePacket(RIGHT, false));
            }
        });
    }

    public void update(double dt){
        totalFpsUpdateTime += dt;
        totalFpsUpdateFrames++;

        if(paused) return;

        if(totalFpsUpdateTime >= fpsUpdateDelay){
            fps = 1000 * totalFpsUpdateFrames / totalFpsUpdateTime;
            totalFpsUpdateFrames = 0;
            totalFpsUpdateTime = 0;
        }
    }

    public void draw(Graphics2D g){
        g.setColor(Color.white);
        g.setFont(defaultFont);

        g.drawString(String.format("FPS: %.0f", fps), 2, 11);
        //g.translate(offset.x, offset.y);

        getWorld().draw(g);
    }



    public void run() {
        logger.out("thread start");

        networkManager.startServerInstance();
        long frameStart, lastFrameUpdate = System.nanoTime(), threadWait;

        while(running){
            frameStart = System.nanoTime();

            update((double)(frameStart - lastFrameUpdate) / 1000000);
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

        try{
            networkManager.disconnect();
            world.getConnectionListener().shutdown();
            Thread.sleep(100);
        }catch (IOException | InterruptedException e){
            logger.err("Failed to shut down client connection listener");
        }
        logger.out("thread stop");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw((Graphics2D) g);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Vector2i temp = new Vector2i(mouseEvent.getX(), mouseEvent.getY());
        if(mousePressed){
            mousePressed = false;
            dm.set(temp);
        }else{
            offset.add(temp).sub(dm);
            dm.set(temp);
        }
    }

    public void setFpsUpdateDelay(double fpsUpdateDelay) {
        this.fpsUpdateDelay = fpsUpdateDelay;
    }

    public void setFPSTarget(double target) {
        this.targetMillis = 1000 / target;
    }

    public void shutdown(){
        logger.dbg("shutdown");
        running = false;
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mouse.set(mouseEvent.getX(), mouseEvent.getY()).sub(offset);
    }

    public World getWorld() {
        return world;
    }

    public ClientNetworkManager getNetworkManager() {
        return networkManager;
    }

    public InetSocketAddress getListenerAddress(){
        return world.getConnectionListener().getListenerAddress();
    }
}
