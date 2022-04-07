package prj.net.packet;

import prj.world.World;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PacketReceiver extends Thread{
    private final Socket sessionSocket;
    private final World localWorld;

    public PacketReceiver(Socket sessionSocket, World localWorld) {
        this.sessionSocket = sessionSocket;
        this.localWorld = localWorld;
    }

    @Override
    public void run() {
        try {
            Packet packet = Packet.receive(sessionSocket);
            if(packet != null){
                if(localWorld.isServerWorld()){
                    if(packet instanceof LoginPacket lp){
                        localWorld.getServerInstance().getNetworkManager().clientLogin(lp.getClientAddress());
                    }else if(packet instanceof LogoutPacket lp){
                        localWorld.getServerInstance().getNetworkManager().clientLogout(lp.getClientAddress());
                    }
                }else{
                    if(packet instanceof ServerShutdownPacket ssp){
                        localWorld.getClientInstance().getNetworkManager().shutdown();
                    }
                }

                List<PacketType> returnPacketTypes;

                if(packet instanceof MultiPacket mp){
                    returnPacketTypes = new ArrayList<>();

                    for (int i = 0; i < mp.getNumberOfPackets(); i++) {
                        packet = Packet.receive(sessionSocket);

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
                    Packet.send(sessionSocket, localWorld.preparePackets(returnPacketTypes));
                }
            }
        } catch (IOException e) {
            System.err.println("Error while receiving packet: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Received data is not of class type");
        }

        try {
            sessionSocket.close();
        } catch (IOException e) {
            System.err.println("Failed to close socket");
        }
    }
}
