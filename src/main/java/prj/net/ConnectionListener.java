package prj.net;

import prj.log.Logger;
import prj.net.packet.PacketReceiver;
import prj.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener extends Thread {
    private final Logger logger = new Logger("");
    private final World world;
    private ServerSocket listenerSocket;

    private boolean running;

    public ConnectionListener(World world, int listenerPort, int listenerBacklog) {
        logger.setName((world.isServerWorld() ? "Server" : "Client") + " connection listener").dbg("init start");

        this.world = world;
        try {
            listenerSocket = new ServerSocket(listenerPort, listenerBacklog);
        }catch (IOException e){
            logger.err("could not initialize listener socket: " + e.getMessage());
        }

        running = listenerSocket != null;

        logger.out("listening for connections at " + getListenerAddress());

        logger.dbg("init end");
    }

    @Override
    public void run(){
        logger.out("thread start");

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

        logger.out("thread stop");
    }

    public boolean doesListenerSocketFailed(){
        return listenerSocket == null;
    }

    public void shutdown() throws IOException {
        logger.dbg("shutdown");
        running = false;
        listenerSocket.close();
    }

    public InetSocketAddress getListenerAddress(){
        return new InetSocketAddress(listenerSocket.getInetAddress(), listenerSocket.getLocalPort());
    }
}
