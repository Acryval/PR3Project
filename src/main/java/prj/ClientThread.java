package prj;

import org.joml.Vector2i;
import prj.gamestates.GameState;
import prj.gamestates.GameStateManager;
import prj.log.Logger;
import prj.net.ClientNetworkManager;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.net.packet.gamestate.ScreenDimensionPacket;
import prj.net.packet.player.PlayerMovePacket;
import prj.world.Camera;
import prj.world.Direction;
import prj.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class ClientThread extends GameState {
    public static ClientThread instance = null;

    private final Logger logger = new Logger("");
    private World world;
    private Camera cam;
    private ClientNetworkManager networkManager;
    private boolean paused;

    public Vector2i scrSize;

    private Font defaultFont;

    @Override
    public GameState init(List<Packet> dataIn) {
        logger.setName("Client").dbg("init start");
        instance = this;

        paused = false;

        defaultFont = new Font("Arial", Font.PLAIN, 11);
        scrSize = new Vector2i();

        world = new World();
        networkManager = new ClientNetworkManager();

        cam = new Camera();
        cam.attachTo(world.getState().getPlayer().getPos());
        networkManager.startServerInstance();

        logger.dbg("init end");
        return super.init(dataIn);
    }

    public int getWidth(){
        return scrSize.x;
    }

    public int getHeight(){
        return scrSize.y;
    }

    public void update(double dt){
        if(paused) return;

        cam.setPos(world.getState().getPlayer().getPos());
    }

    @Override
    public void processPackets(List<Packet> dataIn) {
        for(Packet p : dataIn){
            if (p.getType() == PacketType.scrDimension) {
                Dimension d = ((ScreenDimensionPacket) p).getScreenDimension();
                scrSize = new Vector2i(d.width, d.height);
            }
        }
    }

    @Override
    public List<Packet> unload(List<Packet> endData) {
        shutdown();
        return endData;
    }

    @Override
    public void setActions(InputMap im, ActionMap am) {
        GameStateManager.instance.addMouseListener(cam);
        GameStateManager.instance.addMouseMotionListener(cam);

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
                GameStateManager.instance.stop();
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

    public void draw(Graphics2D g, int width, int height){
        g.setColor(Color.black);
        g.setFont(defaultFont);

        Vector2i off = cam.getGlobalOffset();

        if(Prj.SHOWFPS) {
            g.drawString(String.format("FPS: %.0f", GameStateManager.instance.getFps()), 2, 11);
        }
        if(Prj.DEBUG) {
            g.drawString(String.format("mx: %d, my: %d", cam.getMouse().x - width / 2, height / 2 - cam.getMouse().y), 2, 23);
            g.drawString(String.format("offx: %d, offy: %d", cam.getOffset().x, cam.getOffset().y), 2, 35);
            g.drawString(String.format("px: %d, py: %d", -(int) cam.getPos().x, (int) cam.getPos().y), 2, 47);
        }

        g.translate(width/2, height/2);
        g.translate(off.x, off.y);

        world.draw(g);
    }

    public void shutdown(){
        logger.dbg("shutdown");
        networkManager.shutdown();
    }

    public World getWorld() {
        return world;
    }
}
