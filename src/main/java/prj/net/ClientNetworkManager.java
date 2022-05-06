package prj.net;

import prj.ClientThread;
import prj.ServerThread;
import prj.log.Logger;
import prj.net.packet.Packet;
import prj.net.packet.system.LogoutPacket;
import prj.net.packet.world.GetWorldStatePacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientNetworkManager extends Thread {
    public static final int DEFAULT_BACKLOG = 32;

    private final Logger logger = new Logger("");
    private Socket serverSocket;
    private ServerThread serverThread;

    private boolean running;

    public ClientNetworkManager() {
        logger.setName("Client network manager").dbg("init start");

        serverSocket = null;
        serverThread = null;
        running = true;

        logger.dbg("init end");
    }

    public void send(List<Packet> packets){
        if(serverSocket != null) {
            Packet.send(logger, serverSocket, packets);
        }
    }

    public void send(Packet...packets){
        send(List.of(packets));
    }

    public void startServerInstance(){
        if(serverThread != null){
            logger.err("Cannot start a second server thread");
            return;
        }

        if(serverSocket != null){
            logger.warn("Starting a server thread while being connected");
            disconnect();
        }

        try {
            serverThread = new ServerThread(DEFAULT_BACKLOG);
            serverThread.start();
            Thread.sleep(500);
        }catch (IOException | InterruptedException e) {
            logger.err("Cannot start server thread");
            serverThread = null;
            running = false;
            return;
        }

        connectTo(serverThread.getNetworkManager().getListenerAddress());
        send(new GetWorldStatePacket());
    }

    public void stopServerInstance(){
        disconnect();
        if(serverThread != null) {
            serverThread.shutdown();
            serverThread = null;
        }
    }

    public void connectTo(InetSocketAddress serverAddress) {
        logger.out("trying to connect to " + serverAddress);
        if(serverSocket != null){
            disconnect();
        }

        try {
            serverSocket = new Socket(serverAddress.getHostName(), serverAddress.getPort());
            running = true;
            logger.out("connected");
            this.start();
        }catch (IOException e){
            logger.err("cannot connect to " + serverAddress + ", " + e.getMessage());
            serverSocket = null;
            running = false;
        }
    }

    public void disconnect() {
        if(serverSocket != null && !serverSocket.isClosed()){
            logger.out("disconnecting from " + new InetSocketAddress(serverSocket.getInetAddress(), serverSocket.getPort()));
            send(new LogoutPacket());
        }

        serverSocket = null;
        running = false;
    }

    @Override
    public void run() {
        logger.dbg("thread start");
        List<Packet> receivedPackets = new ArrayList<>();

        while(running){
            try {
                if (serverSocket != null && serverSocket.getInputStream().available() > 0) {
                    receivedPackets.addAll(Packet.receive(logger, serverSocket));
                    if (ClientThread.instance.getWorld().applyPacketData(receivedPackets)) {
                        disconnect();
                    }
                    receivedPackets.clear();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        logger.dbg("thread stop");
    }

    public void shutdown(){
        logger.dbg("shutdown");
        stopServerInstance();
    }
}
