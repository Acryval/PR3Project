package prj;

import prj.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{
    private final World world;
    private final List<InetSocketAddress> clientAddresses;

    private boolean running;

    public ServerThread(World localWorld, int listenerPort, int listenerBacklog) throws IOException {
        world = new World(localWorld, listenerPort, listenerBacklog);
        clientAddresses = new ArrayList<>();
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
            world.getConnectionListener().shutdown();
        } catch (IOException e) {
            System.out.println("Failed to shutdown server connection listener");
        }
    }

    public void shutdown(){
        running = false;
    }

    public InetSocketAddress getSelfAddress(){
        return world.getConnectionListener().getListenerAddress();
    }
}
