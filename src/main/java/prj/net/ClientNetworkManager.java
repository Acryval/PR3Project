package prj.net;

import prj.ClientThread;
import prj.ServerThread;
import prj.log.Logger;
import prj.net.packet.system.LoginPacket;
import prj.net.packet.system.LogoutPacket;
import prj.net.packet.Packet;
import prj.net.packet.PacketSender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ClientNetworkManager {
    private final Logger logger = new Logger("");
    private final ClientThread clientInstance;
    private InetSocketAddress serverAddress;
    private ServerThread serverThread;

    public ClientNetworkManager(ClientThread clientInstance, InetSocketAddress serverAddress) {
        logger.setName("Client network manager").dbg("init start, connected to " + serverAddress);
        this.clientInstance = clientInstance;
        this.serverAddress = serverAddress;
        logger.dbg("init end");
    }

    public ClientNetworkManager(ClientThread clientInstance) {
        logger.setName("Client network manager").dbg("init start, local server");
        this.clientInstance = clientInstance;
        this.serverAddress = new InetSocketAddress("localhost", 0);
        logger.dbg("init end");
    }

    public void send(List<Packet> packets){
        try {
            if(packets.size() > 0) {
                logger.announcePackets(serverAddress, "sending packets", packets);
                new PacketSender(new Socket(serverAddress.getHostName(), serverAddress.getPort()), clientInstance.getWorld(), packets).start();
            }
        }catch (IOException e){
            logger.err("failed to send packets to " + serverAddress + ": " + e.getMessage());
        }
    }

    public void send(Packet...packets){
        send(List.of(packets));
    }

    public void startServerInstance(){
        if(serverThread != null){
            logger.err("cannot have more than one server instace running");
            return;
        }

        if(!serverAddress.getHostName().equals("localhost")){
            logger.err("cannot start a server instance on a remote address");
            return;
        }

        try {
            serverThread = new ServerThread(clientInstance.getWorld(), serverAddress.getPort(), 10);
            serverAddress = serverThread.getListenerAddress();

            serverThread.start();

            send(new LoginPacket(clientInstance.getListenerAddress()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }

    public ServerThread getServerThread() {
        return serverThread;
    }

    public void shutdown(){
        logger.dbg("server shutdown");
        send(new LogoutPacket(clientInstance.getListenerAddress()));

        if(serverThread != null){
            serverThread.shutdown();
            serverThread = null;
        }
    }
}
