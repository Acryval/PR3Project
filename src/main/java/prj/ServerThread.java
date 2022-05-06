package prj;

import prj.log.Logger;
import prj.net.ServerNetworkManager;
import prj.world.World;

import java.io.IOException;

public class ServerThread extends Thread {
    public static ServerThread instance = null;

    private final Logger logger = new Logger("");
    private final World world;
    private final ServerNetworkManager networkManager;
    private double targetMillis;
    private boolean running;

    public ServerThread(int listenerBacklog) throws IOException {
        logger.setName("Server").dbg("init start");
        instance = this;

        world = new World(this);
        networkManager = new ServerNetworkManager(listenerBacklog);
        running = true;

        targetMillis = 1000 / 30.0;
        logger.dbg("init end");
    }

    @Override
    public void run() {
        logger.out("thread start");

        networkManager.start();
        long frameStart, lastFrameStart = System.nanoTime(), threadWait;

        while(running){
            frameStart = System.nanoTime();

            world.updateState((double)(frameStart - lastFrameStart)/1000000000);

            lastFrameStart = frameStart;

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

    public ServerThread setTargetFPS(double target){
        this.targetMillis = 1000 / target;
        return this;
    }

    public void shutdown(){
        logger.dbg("shutdown");
        running = false;
    }

    public World getWorld() {
        return world;
    }

    public ServerNetworkManager getNetworkManager() {
        return networkManager;
    }
}
