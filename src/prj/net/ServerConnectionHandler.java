package prj.net;

import prj.world.World;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ServerConnectionHandler extends Thread{
    private final ServerThread serverInstance;

    private boolean running;

    public ServerConnectionHandler(ServerThread serverInstance) {
        this.serverInstance = serverInstance;
        running = true;

        this.start();
    }

    @Override
    public void run(){;
        //TODO make logger
        System.out.println("[" + new Date(System.currentTimeMillis()) + "] " + "Server connection handler thread started");
        while(running){
            try {
                new ServerConnection(this, serverInstance.getServerSocket().accept());
            } catch (IOException e) {
                System.out.println("Failed to accept connection at: " + new Date(System.currentTimeMillis()));
            }
        }
        //TODO make logger
        System.out.println("[" + new Date(System.currentTimeMillis()) + "] " + "Server connection handler thread stopped");
    }

    public void shutdown(){
        running = false;
    }

    public ServerThread getServerInstance() {
        return serverInstance;
    }
}
