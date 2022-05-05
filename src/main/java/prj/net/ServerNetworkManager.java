package prj.net;

import prj.ServerThread;
import prj.log.Logger;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.net.packet.system.ServerShutdownPacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerNetworkManager extends Thread {
    private final Logger logger = new Logger("");

    private final ServerConnectionListener listener;
    private final List<Socket> clients, pending;
    private boolean running;

    public ServerNetworkManager(int listenerBacklog) {
        logger.setName("Server network manager").dbg("init start");

        this.listener = new ServerConnectionListener(listenerBacklog);
        this.clients = new ArrayList<>();
        this.pending = new ArrayList<>();

        running = listener.isRunning();

        logger.dbg("init end");
    }

    public void broadcast(List<Packet> packets){
        synchronized (clients) {
            for (Socket client : clients) {
                Packet.send(logger, client, packets);
            }
        }
    }

    public void broadcast(Packet...packets){
        broadcast(List.of(packets));
    }

    public InetSocketAddress getListenerAddress(){
        return listener.getListenerAddress();
    }

    public void shutdown(){
        logger.dbg("shutdown");
        broadcast(new ServerShutdownPacket());
        listener.shutdown();
        running = false;
    }

    public void clientLogin(Socket client){
        logger.out("Client login from " + client.getInetAddress());
        pending.add(client);
    }

    @Override
    public void run() {
        listener.start();

        List<Socket> clientLoggingOut = new ArrayList<>();
        List<Packet> receivedPackets = new ArrayList<>();
        List<PacketType> expectedPackets = new ArrayList<>();

        while(running){
            for (Socket s : clients) {
                if(!s.isConnected() || s.isClosed()){
                    clientLoggingOut.add(s);
                    continue;
                }

                try {
                    if(s.getInputStream().available() > 0){
                        receivedPackets.addAll(Packet.receive(logger, s));
                        for(Packet p : receivedPackets){
                            expectedPackets.addAll(p.getExpectedReturnPackets());
                        }

                        if(ServerThread.instance.getWorld().applyPacketData(receivedPackets)){
                            clientLoggingOut.add(s);
                        }

                        Packet.send(logger, s, ServerThread.instance.getWorld().preparePackets(expectedPackets));

                        receivedPackets.clear();
                        expectedPackets.clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            synchronized (clients) {
                clients.removeAll(clientLoggingOut);
                clients.addAll(pending);
            }
            clientLoggingOut.clear();
            pending.clear();
        }
    }
}
