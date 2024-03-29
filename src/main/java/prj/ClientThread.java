package prj;

import org.joml.Vector2i;
import prj.db.DBUtils;
import prj.gamestates.GameState;
import prj.gamestates.GameStateManager;
import prj.log.Logger;
import prj.net.ClientNetworkManager;
import prj.net.packet.Packet;
import prj.net.packet.entity.player.PlayerMovePacket;
import prj.net.packet.gamestate.ConnectToServerPacket;
import prj.net.packet.gamestate.ScreenDimensionPacket;
import prj.net.packet.gamestate.SetUsernamePacket;
import prj.net.packet.gamestate.SetWorldNamePacket;
import prj.net.packet.world.WorldStatePacket;
import prj.world.Camera;
import prj.world.Direction;
import prj.world.World;
import prj.world.WorldState;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.util.List;

public class ClientThread extends GameState {
    public static ClientThread instance = null;

    private final Logger logger = new Logger("");
    private World world;
    private Camera cam;
    private ClientNetworkManager networkManager;
    public Vector2i scrSize;
    private Font defaultFont;
    private String username;
    private String worldName;
    private boolean hosting;

    public String getUsername() {
        return username;
    }

    public int getWidth(){
        return scrSize.x;
    }

    public int getHeight(){
        return scrSize.y;
    }

    public void processPackets(List<Packet> dataIn) {
        logger.setName("Client").dbg("init start");
        instance = this;

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        GameStateManager.instance.setCursor(blankCursor);

        defaultFont = new Font("Arial", Font.PLAIN, 11);
        scrSize = new Vector2i();

        this.username = "";
        boolean startServer = false;
        InetSocketAddress address = null;

        for(Packet data : dataIn){
            switch (data.getType()){
                case setUsername -> this.username = ((SetUsernamePacket)data).getUsername();
                case setWorldName -> this.worldName = ((SetWorldNamePacket)data).getWorldName();
                case startServer -> startServer = true;
                case connectToServer -> address = ((ConnectToServerPacket)data).getAddress();
                case scrDimension -> {
                    Dimension d = ((ScreenDimensionPacket)data).getScreenDimension();
                    scrSize = new Vector2i(d.width, d.height);
                }
                default -> {}
            }
        }

        world = new World(worldName);
        networkManager = new ClientNetworkManager();
        cam = new Camera();
        cam.attach();

        GameStateManager.instance.addMouseListener(cam);
        GameStateManager.instance.addMouseMotionListener(cam);

        this.hosting = true;
        if(startServer) {
            networkManager.startServerInstance();
        }else if(address != null){
            this.hosting = false;
            networkManager.connectTo(address);
        }

        if(hosting && GameStateManager.instance.isDbAvailable()){
            logger.dbg("loading world: " + worldName + " from cloud");
            EntityManager em = GameStateManager.instance.getEmf().createEntityManager();
            em.getTransaction().begin();

            WorldState state = DBUtils.loadState(username, worldName, em);

            if(state.worldName.isEmpty()) {
                logger.dbg("cannot find world:" + worldName);
                logger.dbg("saving new world to db");
                DBUtils.saveState(username, world, em);
            }else{;
                world.applyPacketData(List.of(new WorldStatePacket(state)));
            }

            em.getTransaction().commit();
            em.close();
            logger.dbg("load complete");
        }

        logger.dbg("init end");
    }

    public void setActions(InputMap im, ActionMap am) {
        im.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "exit");

        im.put(KeyStroke.getKeyStroke("pressed W"), "pw");
        im.put(KeyStroke.getKeyStroke("released W"), "rw");
        im.put(KeyStroke.getKeyStroke("pressed A"), "pa");
        im.put(KeyStroke.getKeyStroke("released A"), "ra");
        im.put(KeyStroke.getKeyStroke("pressed D"), "pd");
        im.put(KeyStroke.getKeyStroke("released D"), "rd");

        im.put(KeyStroke.getKeyStroke("shift pressed W"), "pw");
        im.put(KeyStroke.getKeyStroke("shift released W"), "rw");
        im.put(KeyStroke.getKeyStroke("shift pressed A"), "pa");
        im.put(KeyStroke.getKeyStroke("shift released A"), "ra");
        im.put(KeyStroke.getKeyStroke("shift pressed D"), "pd");
        im.put(KeyStroke.getKeyStroke("shift released D"), "rd");

        im.put(KeyStroke.getKeyStroke("ctrl pressed W"), "pw");
        im.put(KeyStroke.getKeyStroke("ctrl released W"), "rw");
        im.put(KeyStroke.getKeyStroke("ctrl pressed A"), "pa");
        im.put(KeyStroke.getKeyStroke("ctrl released A"), "ra");
        im.put(KeyStroke.getKeyStroke("ctrl pressed D"), "pd");
        im.put(KeyStroke.getKeyStroke("ctrl released D"), "rd");

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

        am.put("pw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!world.getLocalPlayer().isKeyUp()) {
                    world.getLocalPlayer().setKeyUp(true);
                    networkManager.send(new PlayerMovePacket(username, Direction.UP, true));
                }
            }
        });
        am.put("rw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(world.getLocalPlayer().isKeyUp()) {
                    world.getLocalPlayer().setKeyUp(false);
                    networkManager.send(new PlayerMovePacket(username, Direction.UP, false));
                }
            }
        });
        am.put("pa", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!world.getLocalPlayer().isKeyLeft()) {
                    world.getLocalPlayer().setKeyLeft(true);
                    networkManager.send(new PlayerMovePacket(username, Direction.LEFT, true));
                }
            }
        });
        am.put("ra", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(world.getLocalPlayer().isKeyLeft()) {
                    world.getLocalPlayer().setKeyLeft(false);
                    networkManager.send(new PlayerMovePacket(username, Direction.LEFT, false));
                }
            }
        });
        am.put("pd", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!world.getLocalPlayer().isKeyRight()) {
                    world.getLocalPlayer().setKeyRight(true);
                    networkManager.send(new PlayerMovePacket(username, Direction.RIGHT, true));
                }
            }
        });
        am.put("rd", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(world.getLocalPlayer().isKeyRight()) {
                    world.getLocalPlayer().setKeyRight(false);
                    networkManager.send(new PlayerMovePacket(username, Direction.RIGHT, false));
                }
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
        Vector2i off = cam.getGlobalOffset();

        g.translate(width/2, height/2);
        g.translate(off.x, off.y);

        world.draw(g);
        world.getLocalPlayer().getItemBar().draw(g);
        cam.draw(g);

        g.translate(-width/2, -height/2);
        g.translate(-off.x, -off.y);

        g.setColor(Color.black);
        g.setFont(defaultFont);
        if(hosting && ServerThread.instance != null){
            g.drawString(networkManager.getServerAddressString(), 7, 76);
        }

        if(Prj.SHOWFPS) {
            g.drawString(String.format("FPS: %.0f", GameStateManager.instance.getFps()), 7, 88);
        }
        if(Prj.DEBUG) {
            g.drawString(String.format("mx: %d, my: %d", cam.getMouse().x - width / 2, height / 2 - cam.getMouse().y), 7, 100);
            g.drawString(String.format("offx: %d, offy: %d", cam.getOffset().x, cam.getOffset().y), 7, 112);
            g.drawString(String.format("px: %d, py: %d", -(int) cam.getPos().x, (int) cam.getPos().y), 7, 124);
        }
    }

    public void update(double dt){
        world.updateState(dt);
        cam.update(world.getState(), world.getLocalPlayer());
    }

    public List<Packet> unload(List<Packet> endData) {
        shutdown();
        GameStateManager.instance.removeMouseListener(cam);
        GameStateManager.instance.removeMouseMotionListener(cam);
        return endData;
    }

    public void shutdown(){
        logger.dbg("shutdown");
        networkManager.shutdown();

        if(hosting && GameStateManager.instance.isDbAvailable()){
            logger.dbg("sync database start");
            EntityManager em = GameStateManager.instance.getEmf().createEntityManager();
            em.getTransaction().begin();

            DBUtils.saveState(username, world, em);

            em.getTransaction().commit();
            em.close();
            logger.dbg("sync database end");
        }
    }

    public World getWorld() {
        return world;
    }

    public Camera getCam() {
        return cam;
    }

    public ClientNetworkManager getNetworkManager() {
        return networkManager;
    }

    public boolean isHosting(){
        return hosting;
    }
}
