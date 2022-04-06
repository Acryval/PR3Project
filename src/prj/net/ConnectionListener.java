package prj.net;

import prj.net.packet.Packet;
import prj.net.packet.PacketReceiver;
import prj.net.packet.PacketSender;
import prj.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ConnectionListener extends Thread{
    private final World world;
    private final ServerSocket listenerSocket;

    private boolean running;

    public ConnectionListener(World world, int listenerPort, int listenerBacklog) throws IOException {
        this.world = world;
        listenerSocket = new ServerSocket(listenerPort, listenerBacklog);
        running = true;
    }

    @Override
    public void run(){
        while(running){
            try {
                new PacketReceiver(listenerSocket.accept(), world).start();
            } catch (IOException e) {
                System.err.println("Failed to accept connection at: " + new Date(System.currentTimeMillis()));
            }
        }

        if(!listenerSocket.isClosed()){
            try {
                listenerSocket.close();
            } catch (IOException e) {
                System.err.println("Failed to close socket");
            }
        }
    }

    public void shutdown() throws IOException {
        running = false;
        listenerSocket.close();
    }

    public InetSocketAddress getListenerAddress(){
        return new InetSocketAddress(listenerSocket.getInetAddress(), listenerSocket.getLocalPort());
    }

    public static <T extends Packet> void sendData(InetSocketAddress receiverAddress, World localWorld, T data) throws IOException {
        new PacketSender<>(new Socket(receiverAddress.getHostName(), receiverAddress.getPort()), localWorld, data).start();
    }
}
