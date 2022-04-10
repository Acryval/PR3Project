package prj.net;

import prj.log.Logger;
import prj.net.packet.PacketReceiver;
import prj.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ConnectionListener extends Thread {
    private final Logger logger = new Logger("");
    private final World world;
    private final ServerSocket listenerSocket;

    private boolean running;

    public ConnectionListener(World world, int listenerPort, int listenerBacklog) throws IOException {
        logger.setName((world.isServerWorld() ? "Server" : "Client") + " connection listener").dbg("init start");

        this.world = world;
        listenerSocket = new ServerSocket(listenerPort, listenerBacklog);
        running = true;

        logger.dbg("listening for connections at " + getListenerAddress());

        logger.dbg("init end");
    }

    @Override
    public void run(){
        logger.dbg("thread start");

        while(running){
            try {
                Socket s = listenerSocket.accept();
                logger.dbg("connection from: " + s.getInetAddress());
                new PacketReceiver(s, world).start();
            } catch (IOException e) {
                if(!listenerSocket.isClosed()){
                    logger.err("failed to accept connection: " + e.getMessage());
                }
            }
        }

        if(!listenerSocket.isClosed()){
            try {
                listenerSocket.close();
                logger.dbg("socket closed");
            } catch (IOException e) {
                logger.err("failed to close socket: " + e.getMessage());
            }
        }

        logger.dbg("thread stop");
    }

    public void shutdown() throws IOException {
        logger.dbg("shutdown");
        running = false;
        listenerSocket.close();
    }

    public InetSocketAddress getListenerAddress(){
        return new InetSocketAddress(listenerSocket.getInetAddress(), listenerSocket.getLocalPort());
    }

    public static int getRandomPort(){
        return new Random().nextInt(50000, 59999);
    }
}
