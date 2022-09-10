package prj;

import org.joml.Vector2i;
import prj.entity.Player;
import prj.gamestates.GameState;
import prj.gamestates.GameStateManager;
import prj.item.Block;
import prj.item.Item;
import prj.item.Pickaxe;
import prj.log.Logger;
import prj.net.ClientNetworkManager;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.net.packet.gamestate.ConnectToServerPacket;
import prj.net.packet.gamestate.ScreenDimensionPacket;
import prj.net.packet.gamestate.SetUsernamePacket;
import prj.wall.DefaultBreakableWall;
import prj.wall.DefaultTransparentWall;
import prj.wall.Wall;
import prj.world.Camera;
import prj.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetSocketAddress;
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
    private String username;

    @Override
    public GameState init(List<Packet> dataIn) {
        logger.setName("Client").dbg("init start");
        instance = this;

        paused = false;

        defaultFont = new Font("Arial", Font.PLAIN, 11);
        scrSize = new Vector2i();

        world = new World();

        this.username = "";
        boolean startServer = false;
        InetSocketAddress address = null;

        loop: for(Packet data : dataIn){
            switch (data.getType()){
                case setUsername -> this.username = ((SetUsernamePacket)data).getUsername();
                case startServer -> startServer = true;
                case connectToServer -> address = ((ConnectToServerPacket)data).getAddress();
                case passData -> {
                    break loop;
                }
                default -> {}
            }
        }

        networkManager = new ClientNetworkManager();

        cam = new Camera();
        cam.attach();

        GameStateManager.instance.addMouseListener(cam);
        GameStateManager.instance.addMouseMotionListener(cam);

        if(startServer) {
            networkManager.startServerInstance();
        }else if(address != null){
            networkManager.connectTo(address);
        }

        logger.dbg("init end");
        return super.init(passRest(dataIn));
    }

    public String getUsername() {
        return username;
    }

    public int getWidth(){
        return scrSize.x;
    }

    public int getHeight(){
        return scrSize.y;
    }

    public void update(double dt){
        if(paused) return;

        world.updateState(dt);
        cam.update(world.getState(), world.getLocalPlayer());
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
        GameStateManager.instance.removeMouseListener(cam);
        GameStateManager.instance.removeMouseMotionListener(cam);
        return endData;
    }

    @Override
    public void setActions(InputMap im, ActionMap am) {
        im.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "exit");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK), "pause");

        im.put(KeyStroke.getKeyStroke("pressed W"), "pw");
        im.put(KeyStroke.getKeyStroke("released W"), "rw");
        im.put(KeyStroke.getKeyStroke("pressed A"), "pa");
        im.put(KeyStroke.getKeyStroke("released A"), "ra");
        im.put(KeyStroke.getKeyStroke("pressed D"), "pd");
        im.put(KeyStroke.getKeyStroke("released D"), "rd");
        im.put(KeyStroke.getKeyStroke("pressed 0"), "p0");
        im.put(KeyStroke.getKeyStroke("pressed 1"), "p1");
        im.put(KeyStroke.getKeyStroke("pressed 2"), "p2");
        im.put(KeyStroke.getKeyStroke("pressed 3"), "p3");
        im.put(KeyStroke.getKeyStroke("pressed 4"), "p4");
        im.put(KeyStroke.getKeyStroke("pressed 5"), "p5");
        im.put(KeyStroke.getKeyStroke("pressed 6"), "p6");
        im.put(KeyStroke.getKeyStroke("pressed 7"), "p7");
        im.put(KeyStroke.getKeyStroke("pressed 8"), "p8");
        im.put(KeyStroke.getKeyStroke("pressed 9"), "p9");

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
                world.getLocalPlayer().setKeyUp(true);
            }
        });
        am.put("rw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().setKeyUp(false);
            }
        });
        am.put("pa", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().setKeyLeft(true);
            }
        });
        am.put("ra", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().setKeyLeft(false);
            }
        });
        am.put("pd", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().setKeyRight(true);
            }
        });
        am.put("rd", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().setKeyRight(false);
            }
        });
        am.put("p0", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(9);
            }
        });
        am.put("p1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(0);
            }
        });
        am.put("p2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(1);
            }
        });
        am.put("p3", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(2);
            }
        });
        am.put("p4", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(3);
            }
        });
        am.put("p5", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(4);
            }
        });
        am.put("p6", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(5);
            }
        });
        am.put("p7", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(6);
            }
        });
        am.put("p8", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(7);
            }
        });
        am.put("p9", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                world.getLocalPlayer().getItemBar().setInHandItemIndex(8);
            }
        });
    }

    public void draw(Graphics2D g, int width, int height){
        g.setColor(Color.black);
        g.setFont(defaultFont);

        Vector2i off = cam.getGlobalOffset();

        if(Prj.SHOWFPS) {
            g.drawString(String.format("FPS: %.0f", GameStateManager.instance.getFps()), 7, 76);
        }
        if(Prj.DEBUG) {
            g.drawString(String.format("mx: %d, my: %d", cam.getMouse().x - width / 2, height / 2 - cam.getMouse().y), 7, 88);
            g.drawString(String.format("offx: %d, offy: %d", cam.getOffset().x, cam.getOffset().y), 7, 100);
            g.drawString(String.format("px: %d, py: %d", -(int) cam.getPos().x, (int) cam.getPos().y), 7, 112);
        }

        g.translate(width/2, height/2);
        g.translate(off.x, off.y);

        world.draw(g);
        world.getLocalPlayer().getItemBar().draw(g);
        cam.draw(g);
    }

    public void shutdown(){
        logger.dbg("shutdown");
        networkManager.shutdown();

    }

    public World getWorld() {
        return world;
    }

    public Camera getCam() {
        return cam;
    }
}
