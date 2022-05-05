package prj.net;

import prj.ServerThread;
import prj.log.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionListener extends Thread {
    private final Logger logger = new Logger("");
    private ServerSocket listenerSocket;

    private boolean running;

    public ServerConnectionListener(int listenerBacklog) {
        logger.setName("Server connection listener").dbg("init start");

        try {
            listenerSocket = new ServerSocket(0, listenerBacklog);
        }catch (IOException e){
            logger.err("could not initialize listener socket: " + e.getMessage());
            listenerSocket = null;
        }

        running = listenerSocket != null;

        logger.out("listening for connections at " + getListenerAddress());
        logger.dbg("init end");
    }

    public boolean isRunning(){
        return running;
    }

    public void shutdown() {
        logger.dbg("shutdown");
        running = false;
        try {
            listenerSocket.close();
        }catch (IOException e){
            logger.err("failed to close socket: " + e.getMessage());
        }
    }

    public InetSocketAddress getListenerAddress(){
        return new InetSocketAddress(listenerSocket.getInetAddress(), listenerSocket.getLocalPort());
    }

    @Override
    public void run(){
        logger.out("thread start");

        while(running){
            try {
                Socket s = listenerSocket.accept();
                logger.dbg("connection from: " + s.getInetAddress());
                ServerThread.instance.getNetworkManager().clientLogin(s);
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
}
