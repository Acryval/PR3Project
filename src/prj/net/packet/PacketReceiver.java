package prj.net.packet;

import prj.world.World;

import java.io.IOException;
import java.net.Socket;

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

            localWorld.applyPacketData(packet);

            if(packet != null && packet.expectsAnswer()){
                //TODO prepare answer for received packet
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
