package prj.net;

import prj.ClientThread;
import prj.ServerThread;
import prj.log.Logger;
import prj.net.packet.system.LoginPacket;
import prj.net.packet.system.LogoutPacket;
import prj.net.packet.Packet;
import prj.net.packet.PacketSender;
import prj.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ClientNetworkManager {
    private final Logger logger = new Logger("");
    private final World localWorld;
    private InetSocketAddress serverAddress;
    private ServerThread serverThread;

    public ClientNetworkManager(ClientThread clientInstance) {
        logger.setName("Client network manager").dbg("init start");
        this.localWorld = clientInstance.getWorld();
        this.serverAddress = null;
        logger.dbg("init end");
    }

    public void send(List<Packet> packets){
        if(serverAddress == null) return;
        try {
            if(packets.size() > 0) {
                logger.announcePackets(serverAddress, "sending packets", packets);
                new PacketSender(new Socket(serverAddress.getHostName(), serverAddress.getPort()), localWorld, packets).start();
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

        if(serverAddress != null){
            logger.err("cannot start a server instance while being connected to a remote server");
            return;
        }

        try {
            logger.out("Starting local server");
            serverThread = new ServerThread(localWorld, 0, 10);
            serverAddress = serverThread.getListenerAddress();

            serverThread.start();

            send(new LoginPacket(localWorld.getConnectionListener().getListenerAddress()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServerInstance(){
        if(serverThread == null){
            logger.warn("trying to stop a server that isn't running");
        }else{
            logger.out("Stopping local server");
            serverThread.shutdown();
            serverThread = null;
        }
    }

    public void connectTo(InetSocketAddress serverAddress){
        if(serverThread != null){
            logger.err("Trying to connect to a remote server while local instance is running");
            return;
        }

        disconnect();

        logger.out("Connecting to " + serverAddress);
        this.serverAddress = serverAddress;
        send(new LoginPacket(localWorld.getConnectionListener().getListenerAddress()));
    }

    public void disconnect(){
        if(serverThread != null){
            stopServerInstance();
        }else if(this.serverAddress != null){
            logger.out("Disconnecting from " + this.serverAddress);
            send(new LogoutPacket(localWorld.getConnectionListener().getListenerAddress()));
            this.serverAddress = null;
        }else{
            logger.warn("Client isn't connected anywhere");
        }
    }

    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }

    public ServerThread getServerThread() {
        return serverThread;
    }
}
