package prj.net;

import prj.ServerThread;
import prj.net.packet.Packet;
import prj.net.packet.PacketSender;
import prj.net.packet.ServerShutdownPacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerNetworkManager{
    private final ServerThread serverInstance;
    private final List<InetSocketAddress> clientAddresses;

    public ServerNetworkManager(ServerThread serverInstance) {
        this.serverInstance = serverInstance;
        this.clientAddresses = new ArrayList<>();
    }

    public void send(InetSocketAddress clientAddress, Packet...packets){
        try {
            new PacketSender(new Socket(clientAddress.getHostName(), clientAddress.getPort()), serverInstance.getWorld(), packets).start();
        }catch (IOException e){
            System.out.println("Failed to send packets");
        }
    }

    public void broadcast(Packet...packets){
        for(InetSocketAddress clientAddress : clientAddresses){
            send(clientAddress, packets);
        }
    }

    public void clientLogin(InetSocketAddress client){
        if(!clientAddresses.contains(client)) clientAddresses.add(client);
    }

    public void clientLogout(InetSocketAddress client){
        clientAddresses.remove(client);
    }

    public void shutdown(){
        broadcast(new ServerShutdownPacket());
    }
}
