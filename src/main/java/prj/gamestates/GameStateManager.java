package prj.gamestates;

import prj.log.Logger;
import prj.net.packet.Packet;

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

    public GameStateManager(int width, int height) {
        logger.setName("Game State Manager").dbg("init start");
        instance = this;

        currentState = null;
        gameStateRegistry = new HashMap<>();
        loadedStates = new HashMap<>();

        Dimension dim = new Dimension(width, height);

        setBackground(Color.white);
        setPreferredSize(dim);
        setSize(dim);
        setFocusable(true);
        requestFocus();

        im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        am = getActionMap();

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

    public GameState loadState(String name, Packet...initData){
        return loadState(name, List.of(initData));
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
                logger.warn("trying to unloaded an unloaded state: " + name);
            }
        }else{
            logger.err("trying to unloaded an unregistered state: " + name);
        }
        return new ArrayList<>();
    }

    public void setState(String name, Packet...endData){
        if(currentState == null) {
            currentState = loadState(name, List.of(endData));
        }else{
            im.clear();
            am.clear();
            currentState = loadState(name, currentState.unload(endData));
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

    public void shutdown(){
        currentState = null;
        loadedStates.forEach((k, v) -> v.unload());
        loadedStates.clear();
        gameStateRegistry.clear();
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
