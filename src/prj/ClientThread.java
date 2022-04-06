package prj;

import org.joml.Vector2i;
import prj.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ClientThread extends JPanel implements MouseListener, MouseMotionListener {
    private World world;
    private InetSocketAddress serverAddress;

    private InputMap im;
    private ActionMap am;

    private double fpsTargetPrintDelay;
    private double currentFpsTargetPrintDelay;
    private double actualFps;
    private boolean running, paused, mousePressed;
    private Vector2i mouse, dm, offset;

    private Font defaultFont;

    public ClientThread(int width, int height) {
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();

        addMouseListener(this);
        addMouseMotionListener(this);

        initValues();
        loadActions();
    }

    public void initValues(){
        im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        am = getActionMap();

        double fpsTarget = 120;
        running = true;
        paused = false;
        mousePressed = false;

        fpsTargetPrintDelay = 0.2;
        currentFpsTargetPrintDelay = fpsTargetPrintDelay;
        actualFps = fpsTarget;

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
        if(paused) return;

        currentFpsTargetPrintDelay -= dt;
        if(currentFpsTargetPrintDelay <= 0){
            while(currentFpsTargetPrintDelay <= 0){
                currentFpsTargetPrintDelay += fpsTargetPrintDelay;
            }

            actualFps = 1.0 / dt;
        }
    }

    public void draw(Graphics2D g){
        g.setColor(Color.white);
        g.setFont(defaultFont);

        g.drawString(String.format("FPS: %.0f", actualFps), 2, 11);
        g.translate(offset.x, offset.y);

        g.drawString("Mouse: ",2,23);
        g.drawString("x: " + mouse.x,2,35);
        g.drawString("y: " + mouse.y,2,47);

        g.transform(new AffineTransform(1, 0, 0, -1, 0, 0));
    }

    public void run(){
        long start, lastTime = System.nanoTime();

        while(running){
            start = System.nanoTime();

            update((double)(start - lastTime)/1000000000);

            lastTime = start;
            repaint();
        }

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

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mouse.set(mouseEvent.getX(), mouseEvent.getY()).sub(offset);
    }
}
