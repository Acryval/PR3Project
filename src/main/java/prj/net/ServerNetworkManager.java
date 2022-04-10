package prj.net;

import prj.ServerThread;
import prj.log.Logger;
import prj.net.packet.Packet;
import prj.net.packet.PacketSender;
import prj.net.packet.ServerShutdownPacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerNetworkManager {
    private final Logger logger = new Logger("");
    private final ServerThread serverInstance;
    private final List<InetSocketAddress> clientAddresses;

    public ServerNetworkManager(ServerThread serverInstance) {
        logger.setName("Server network manager").dbg("init start");
        this.serverInstance = serverInstance;
        this.clientAddresses = new ArrayList<>();
        logger.dbg("init end");
    }

    public synchronized void send(InetSocketAddress clientAddress, Packet...packets){
        try {
            logger.announcePackets(clientAddress, "sending packets", packets);
            new PacketSender(new Socket(clientAddress.getHostName(), clientAddress.getPort()), serverInstance.getWorld(), packets).start();
        }catch (IOException e){
            logger.warn("failed to send packets to " + clientAddress + ": " + e.getMessage());
        }
    }

    public synchronized void broadcast(Packet...packets){
        for(InetSocketAddress clientAddress : clientAddresses){
            send(clientAddress, packets);
        }
    }

    public synchronized int getActiveClients(){
        return clientAddresses.size();
    }

    public synchronized void clientLogin(InetSocketAddress client){
        if (!clientAddresses.contains(client)) clientAddresses.add(client);
    }

    public synchronized void clientLogout(InetSocketAddress client){
        clientAddresses.remove(client);
    }

    public void shutdown(){
        logger.dbg("shutdown");
        broadcast(new ServerShutdownPacket());
    }
}
