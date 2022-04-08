package prj;

import prj.net.ServerNetworkManager;
import prj.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerThread extends Thread{
    private final World world;
    private final ServerNetworkManager networkManager;
    private double targetMillis;

    private boolean running;

    public ServerThread(World localWorld, int listenerPort, int listenerBacklog) throws IOException {
        world = new World(localWorld, listenerPort, listenerBacklog, this);
        networkManager = new ServerNetworkManager(this);
        running = true;

        targetMillis = 1000 / 60.0;
    }

    @Override
    public void run() {
        long frameStart, lastFrameStart = System.nanoTime(), threadWait;

        world.getConnectionListener().start();

        while(running){
            frameStart = System.nanoTime();

            world.updateState((double)(frameStart - lastFrameStart)/1000000);

            lastFrameStart = frameStart;

            threadWait = (long)(targetMillis - (System.nanoTime() - frameStart) / 1000000);

            if(threadWait < 0){
                System.out.printf("[Server] Lag %dms ( %.2f frames at %.0f FPS ) %n", -threadWait, -threadWait / targetMillis, 1000 / targetMillis);
                threadWait = 0;
            }

            try{
                Thread.sleep(threadWait);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        try {
            networkManager.shutdown();
            world.getConnectionListener().shutdown();
        } catch (IOException e) {
            System.err.println("Failed to shut down server connection listener");
        }
    }

    public ServerThread setTargetFPS(double target){
        this.targetMillis = 1000 / target;
        return this;
    }

    public void shutdown(){
        running = false;
    }

    public World getWorld() {
        return world;
    }

    public ServerNetworkManager getNetworkManager() {
        return networkManager;
    }

    public InetSocketAddress getListenerAddress(){
        return world.getConnectionListener().getListenerAddress();
    }
}
