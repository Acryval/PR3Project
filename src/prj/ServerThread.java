package prj;

import prj.net.ServerNetworkManager;
import prj.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerThread extends Thread{
    private final World world;
    private ServerNetworkManager networkManager;

    private boolean running;

    public ServerThread(World localWorld, int listenerPort, int listenerBacklog) throws IOException {
        world = new World(localWorld, listenerPort, listenerBacklog, this);
        running = true;
    }

    @Override
    public void run() {
        long frameStart, lastFrameStart;
        lastFrameStart = System.nanoTime();

        world.getConnectionListener().start();

        while(running){
            frameStart = System.nanoTime();

            world.updateState((double)(frameStart - lastFrameStart)/1000000000);

            lastFrameStart = frameStart;
        }

        try {
            networkManager.shutdown();
            world.getConnectionListener().shutdown();
        } catch (IOException e) {
            System.out.println("Failed to shutdown server connection listener");
        }
    }

    public void shutdown(){
        running = false;
    }

    public InetSocketAddress getListenerAddress(){
        return world.getConnectionListener().getListenerAddress();
    }

    public World getWorld() {
        return world;
    }

    public ServerNetworkManager getNetworkManager() {
        return networkManager;
    }
}
