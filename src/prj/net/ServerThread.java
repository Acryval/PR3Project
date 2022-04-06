package prj.net;

import prj.world.World;
import prj.world.WorldState;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;

public class ServerThread extends Thread{
    private final World world;
    private final ServerSocket serverSocket;

    private boolean running;

    public ServerThread(int serverPort, int backlog, World localWorld) throws IOException {
        this.world = new World(localWorld);
        serverSocket = new ServerSocket(serverPort, backlog);
        running = true;

        this.start();
    }

    public World getWorld() {
        return world;
    }

    public ServerSocket getServerSocket(){
        return serverSocket;
    }

    public void shutdown(){
        running = false;
    }

    @Override
    public void run() {
        long frameStart, lastFrameStart;
        lastFrameStart = System.nanoTime();
        //TODO make logger
        System.out.println("[" + new Date(System.currentTimeMillis()) + "] " + "Server thread started");

        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(this);

        while(running){
            frameStart = System.nanoTime();

            world.updateState((double)(frameStart - lastFrameStart)/1000000000);

            lastFrameStart = frameStart;
        }

        connectionHandler.shutdown();
        //TODO make logger
        System.out.println("[" + new Date(System.currentTimeMillis()) + "] " + "Server thread stopped");
    }
}
