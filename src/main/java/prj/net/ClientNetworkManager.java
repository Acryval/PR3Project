package prj.net;

import prj.ClientThread;
import prj.ServerThread;
import prj.net.packet.LoginPacket;
import prj.net.packet.LogoutPacket;
import prj.net.packet.Packet;
import prj.net.packet.PacketSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientNetworkManager {
    private final ClientThread clientInstance;
    private final InetSocketAddress serverAddress;
    private ServerThread serverThread;

    public ClientNetworkManager(ClientThread clientInstance, InetSocketAddress serverAddress) {
        this.clientInstance = clientInstance;
        this.serverAddress = serverAddress;
    }

    public ClientNetworkManager(ClientThread clientInstance) {
        this.clientInstance = clientInstance;
        this.serverAddress = new InetSocketAddress("localhost", clientInstance.getWorld().getConnectionListener().getListenerAddress().getPort() + 1);
    }

    public void send(Packet...packets){
        try {
            new PacketSender(new Socket(serverAddress.getHostName(), serverAddress.getPort()), clientInstance.getWorld(), packets).start();
        }catch (IOException e){
            System.err.println("Failed to send packets");
        }
    }

    public void startServerInstance(){
        if(serverThread != null){
            System.out.println("Cannot have more than one server instace running");
            return;
        }

        if(!serverAddress.getHostName().equals("localhost")){
            System.out.println("Cannot start a server instance on a remote address");
            return;
        }

        try {
            serverThread = new ServerThread(clientInstance.getWorld(), serverAddress.getPort(), 10);
            send(new LoginPacket(clientInstance.getListenerAddress()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        send(new LogoutPacket(clientInstance.getListenerAddress()));

        if(serverThread != null){
            serverThread.shutdown();
            serverThread = null;
        }
    }
}
