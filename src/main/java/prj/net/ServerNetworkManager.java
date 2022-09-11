package prj.net;

import prj.ServerThread;
import prj.log.Logger;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.net.packet.system.ServerShutdownPacket;
import prj.world.UserSpecificData;
import prj.world.PreparedPackets;

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
        if(packets.size() == 0) return;
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
        synchronized (pending) {
            pending.add(client);
        }
    }

    @Override
    public void run() {
        logger.dbg("thread start");
        listener.start();

        List<Socket> clientLoggingOut = new ArrayList<>();
        UserSpecificData usd;
        PreparedPackets pp;

        while(running){
            for (Socket s : clients) {
                if(!s.isConnected()) logger.err(s.getInetAddress() + " is not connected");
                if(s.isClosed()) logger.err(s.getInetAddress() + " is closed");

                if(!s.isConnected() || s.isClosed()){
                    clientLoggingOut.add(s);
                    continue;
                }

                try {
                    if (s.getInputStream().available() > 0) {
                        synchronized (clients) {
                            usd = ServerThread.instance.getWorld().applyPacketData(Packet.receive(logger, s));
                            if (usd.isLogout()) {
                                clientLoggingOut.add(s);
                            }

                            pp = ServerThread.instance.getWorld().preparePackets(usd);
                            Packet.send(logger, s, pp.getToSend());
                        }
                        broadcast(pp.getToBroadcast());
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            for(Socket s : clientLoggingOut){
                logger.out("Client logout from " + s.getInetAddress());
            }

            synchronized (clients) {
                clients.removeAll(clientLoggingOut);
                clientLoggingOut.clear();

                synchronized (pending) {
                    clients.addAll(pending);
                    pending.clear();
                }
            }
        }

        logger.dbg("thread stop");
    }
}
