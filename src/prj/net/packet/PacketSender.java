package prj.net.packet;

import prj.world.World;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketSender extends Thread {
    private final Socket sessionSocket;
    private final World localWorld;
    private final List<Packet> dataIn;

    public PacketSender(Socket sessionSocket, World localWorld, Packet... dataIn) {
        this.sessionSocket = sessionSocket;
        this.localWorld = localWorld;
        this.dataIn = new ArrayList<>();

        Collections.addAll(this.dataIn, dataIn);
    }

    @Override
    public void run() {
        try {
            int expectedPacketNum = 0;

            if(dataIn.size() > 1){
                Packet.send(sessionSocket, new MultiPacket(dataIn.size()));
            }

            for(Packet p : dataIn){
                expectedPacketNum += p.expectedReturnPackets.size();
            }

            Packet.send(sessionSocket, dataIn);

            for (int i = 0; i < expectedPacketNum; i++) {
                localWorld.applyPacketData(Packet.receive(sessionSocket));
            }
        } catch (IOException e) {
            System.err.println("Error while processing packet: " + e.getMessage());
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
