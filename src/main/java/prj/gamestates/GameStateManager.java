package prj.gamestates;

import prj.log.Logger;
import prj.net.packet.Packet;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameStateManager extends JPanel {
    public static GameStateManager instance;

    private final Logger logger = new Logger("");
    private GameState currentState;
    private final Map<String, Class<? extends GameState>> gameStateRegistry;
    private final Map<String, GameState> loadedStates;
    private final InputMap im;
    private final ActionMap am;
    private final double fpsUpdateDelay;
    private final double targetMillis;
    private long totalFpsUpdateFrames;
    private double totalFpsUpdateTime;
    private double fps;
    private boolean running;
    private EntityManagerFactory emf;
    private final boolean dbAvailable;

    public GameStateManager(int width, int height) {
        logger.setName("Game State Manager").dbg("init start");
        instance = this;

        currentState = null;
        gameStateRegistry = new HashMap<>();
        loadedStates = new HashMap<>();

        Dimension dim = new Dimension(width, height);
        try {
            emf = Persistence.createEntityManagerFactory("PR3Project-unit");
        }catch (Exception ignored){
            emf = null;
        }
        dbAvailable = emf != null;


        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(dim);
        setSize(dim);
        setFocusable(true);
        requestFocus();

        im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        am = getActionMap();

        fps = 60;
        fpsUpdateDelay = 200;
        totalFpsUpdateFrames = 0;
        totalFpsUpdateTime = 0;

        targetMillis = 1000 / fps;
        running = true;

        logger.dbg("init end");
    }

    public void registerGameState(String name, Class<? extends GameState> state){
        if(gameStateRegistry.containsKey(name)){
            logger.err("trying to override state: " + name);
        }else if(state == null){
            logger.err("trying to register a NULL state");
        }else{
            gameStateRegistry.put(name, state);
        }
    }

    public GameState loadState(String name, List<Packet> initData) {
        logger.dbg("loading state: " + name);
        if (!loadedStates.containsKey(name)) {
            Class<?> state = gameStateRegistry.get(name);
            if (state == null) {
                logger.err("trying to load unregistered state: " + name);
            } else {
                try {
                    loadedStates.put(name, ((GameState) state.getConstructor().newInstance()).init(initData));
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    logger.err("loading state " + name + " failed: " + e.getMessage());
                }
            }
        }
        return loadedStates.get(name);
    }

    public List<Packet> unloadState(String name, List<Packet> endData){
        logger.dbg("unloading state: " + name);
        if(gameStateRegistry.containsKey(name)){
            if(loadedStates.containsKey(name)){
                GameState unloadedState = loadedStates.remove(name);
                return unloadedState.unload(endData);
            }else{
                logger.warn("trying to unload an unloaded state: " + name);
            }
        }else{
            logger.err("trying to unload an unregistered state: " + name);
        }
        return new ArrayList<>();
    }

    public void setState(String name, Packet...data){
        if(currentState == null) {
            currentState = loadState(name, List.of(data));
        }else{
            im.clear();
            am.clear();
            currentState = loadState(name, currentState.unload(data));
        }

        if(currentState != null) {
            currentState.setActions(im, am);
        }else{
            logger.err("could not load state: " + name);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(currentState != null) {
            currentState.draw((Graphics2D) g, getWidth(), getHeight());
        }
    }

    public void stop(){
        running = false;
    }

    public void shutdown(){
        currentState = null;
        loadedStates.forEach((k, v) -> v.unload());
        loadedStates.clear();
        gameStateRegistry.clear();
        if(emf != null)
            emf.close();
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void updateFPS(double dt){
        totalFpsUpdateTime += dt;
        totalFpsUpdateFrames++;

        if(totalFpsUpdateTime*1000 >= fpsUpdateDelay){
            fps = totalFpsUpdateFrames / totalFpsUpdateTime;
            totalFpsUpdateFrames = 0;
            totalFpsUpdateTime = 0;
        }
    }

    public void run() {
        logger.dbg("render start");
        long frameStart, lastFrameUpdate = System.nanoTime(), threadWait;
        double dt;

        while(running){
            frameStart = System.nanoTime();

            dt = (double)(frameStart - lastFrameUpdate) / 1000000000;
            updateFPS(dt);
            if(currentState != null)
                currentState.update(dt);
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

        shutdown();
        logger.dbg("render stop");
    }

    public double getFps() {
        return fps;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public boolean isDbAvailable() {
        return dbAvailable;
    }
}
