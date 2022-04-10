package prj.net.packet;

import prj.log.Logger;
import prj.world.World;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PacketReceiver extends Thread {
    private final Logger logger = new Logger("");
    private final Socket sessionSocket;
    private final World localWorld;

    public PacketReceiver(Socket sessionSocket, World localWorld) {
        logger.setName(String.format("%s packet receiver [%s]", (localWorld.isServerWorld() ? "Server" : "Client"), sessionSocket.getInetAddress()));
        this.sessionSocket = sessionSocket;
        this.localWorld = localWorld;
    }

    @Override
    public void run() {
        logger.dbg("session started");

        try {
            Packet packet = Packet.receive(sessionSocket);

            if(packet != null){

                logger.announcePackets(null, "received", packet);

                if(localWorld.isServerWorld()){
                    if(packet instanceof LoginPacket lp){
                        logger.dbg("LOGIN " + lp.getClientAddress());
                        localWorld.getServerInstance().getNetworkManager().clientLogin(lp.getClientAddress());
                    }else if(packet instanceof LogoutPacket lp){
                        logger.dbg("LOGOUT " + lp.getClientAddress());
                        localWorld.getServerInstance().getNetworkManager().clientLogout(lp.getClientAddress());
                    }
                }else{
                    if(packet instanceof ServerShutdownPacket ssp){
                        logger.dbg("SERVER SHUTDOWN");
                        if(localWorld.getClientInstance().getNetworkManager() != null)
                            localWorld.getClientInstance().getNetworkManager().shutdown();
                    }
                }

                List<PacketType> returnPacketTypes;

                if(packet instanceof MultiPacket mp){
                    logger.dbg(mp.getNumberOfPackets() + " packets incoming");
                    returnPacketTypes = new ArrayList<>();

                    for (int i = 0; i < mp.getNumberOfPackets(); i++) {
                        packet = Packet.receive(sessionSocket);
                        logger.announcePackets(null, String.format("(%d/%d)", (i+1), mp.getNumberOfPackets()), packet);

                        if(packet != null){
                            localWorld.applyPacketData(packet);

                            returnPacketTypes.addAll(packet.expectedReturnPackets);
                        }
                    }
                }else{
                    localWorld.applyPacketData(packet);
                    returnPacketTypes = packet.expectedReturnPackets;
                }

                if(returnPacketTypes.size() > 0){
                    List<Packet> returnPackets = localWorld.preparePackets(returnPacketTypes);

                    logger.announcePackets(null, "sending responses", returnPackets);

                    Packet.send(sessionSocket, returnPackets);
                }
            }else{
                logger.warn("received corrupted packet");
            }
        } catch (IOException e) {
            logger.err("receiving packets failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.err(e.getMessage());
        }

        try {
            sessionSocket.close();
            logger.dbg("session socket closed");
        } catch (IOException e) {
            logger.err("failed to close session socket");
        }

        logger.dbg("session sended");
    }
}
