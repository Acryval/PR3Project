package prj;

import org.joml.Vector2i;
import prj.log.Logger;
import prj.net.ClientNetworkManager;
import prj.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.net.InetSocketAddress;

public class ClientThread extends JPanel implements MouseListener, MouseMotionListener {
    private final Logger logger = new Logger("");
    private World world;
    private ClientNetworkManager networkManager;

    private InputMap im;
    private ActionMap am;

    private long totalFpsUpdateFrames;
    private double fpsUpdateDelay, totalFpsUpdateTime;
    private double fps, targetMillis;
    private boolean running, paused, mousePressed;
    private Vector2i mouse, dm, offset;

    private Font defaultFont;

    public ClientThread(int width, int height) {
        logger.setName("Client").dbg("init start");
        setBackground(Color.black);
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

        running = true;
        paused = false;
        mousePressed = false;

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
    }

    public void loadActions(){
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exit");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK), "pause");

        am.put("exit", new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                running = false;
            }
        });
        am.put("pause", new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                paused ^= true;
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
        g.translate(offset.x, offset.y);

        g.drawString("Mouse: ",2,23);
        g.drawString("x: " + mouse.x,2,35);
        g.drawString("y: " + mouse.y,2,47);

        g.transform(new AffineTransform(1, 0, 0, -1, 0, 0));
    }

    public void run() {
        logger.dbg("thread start");
        long frameStart, lastFrameUpdate = System.nanoTime(), threadWait;

        while(running){
            frameStart = System.nanoTime();

            update((double)(frameStart - lastFrameUpdate) / 1000000);
            repaint();

            lastFrameUpdate = frameStart;

            threadWait = (long)(targetMillis - (System.nanoTime() - frameStart) / 1000000);

            if(threadWait < 0){
                logger.warn(String.format("lag %dms ( %.2f frames at %.0f FPS ) %n", -threadWait, -threadWait / targetMillis, 1000 / targetMillis));
                threadWait = 0;
            }

            try{
                Thread.sleep(threadWait);
            }catch (InterruptedException e){
                logger.err("interrupted: " + e.getMessage());
            }
        }

        /*try{
            networkManager.shutdown();
            world.getConnectionListener().shutdown();
        }catch (IOException e){
            logger.err("Failed to shut down client connection listener");
        }*/

        logger.dbg("thread stop");
        System.exit(0);
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
