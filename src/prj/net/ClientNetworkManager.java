package prj.net;

import prj.net.packet.Packet;
import prj.net.packet.PacketDataType;
import prj.world.WorldState;

import java.io.IOException;
import java.util.Random;

public class ClientNetworkManager {
    private final String serverHost;
    private final int serverPort;
    private int localServerBacklog;
    private ServerThread serverInstance;

    public ClientNetworkManager(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.localServerBacklog = 10;
        this.serverInstance = null;
    }

    public ClientNetworkManager(int serverPort) {
        this("localhost", serverPort);
    }

    public ClientNetworkManager() {
        this(new Random().nextInt(50000, 60000));
    }

    public void setLocalServerBacklog(int localServerBacklog) {
        this.localServerBacklog = localServerBacklog;
    }

    public <T extends PacketDataType> Object send(T data){
        return new Packet<T>(serverHost, serverPort, data).getPossibleData();
    }

    public void startServer(WorldState initialState){
        if(!serverHost.equals("localhost")){
            System.out.println("Cannot start a server on a host other than localhost");
            return;
        }

        if(serverInstance != null){
            System.out.println("You cannot have multiple server instances running");
            return;
        }

        try {
            serverInstance = new ServerThread(serverPort, localServerBacklog, initialState);
        }catch (IOException e){
            System.out.println();
        }
    }

    public void shutdownServer(){
        serverInstance.shutdown();
        serverInstance = null;
    }
}
