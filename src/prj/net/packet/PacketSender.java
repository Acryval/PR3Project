package prj.net.packet;

import prj.world.World;

import java.io.IOException;
import java.net.Socket;

public class PacketSender<T extends Packet> extends Thread {
    private final Socket sessionSocket;
    private final World localWorld;
    private final T dataIn;

    public PacketSender(Socket sessionSocket, World localWorld, T dataIn) {
        this.sessionSocket = sessionSocket;
        this.localWorld = localWorld;
        this.dataIn = dataIn;
    }

    @Override
    public void run() {
        try {
            Packet.send(sessionSocket, dataIn);

            if(dataIn.expectsAnswer()){
                Packet packet = Packet.receive(sessionSocket);

                localWorld.applyPacketData(packet);
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
