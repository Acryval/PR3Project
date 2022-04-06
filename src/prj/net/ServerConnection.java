package prj.net;

import prj.net.packet.Packet;
import prj.net.packet.PacketDataType;
import prj.world.World;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ServerConnection extends Thread{
    private final Socket currentSession;
    private final World world;

    public ServerConnection(ServerConnectionHandler connectionHandler, Socket currentSession) {
        this.currentSession = currentSession;
        world = connectionHandler.getServerInstance().getWorld();

        this.start();
    }

    @Override
    public void run() {
        //TODO make logger
        System.out.println("[" + new Date(System.currentTimeMillis()) + "] " + "Connection made with: " + currentSession.getInetAddress());

        try {
            Object data = Packet.receive(currentSession);

            if(data instanceof PacketDataType){
                PacketDataType packetData = (PacketDataType) data;
                String dataTypeName = packetData.getDataTypeName();

                //TODO make logger
                System.out.println("[" + new Date(System.currentTimeMillis()) + "] " + "Received packet of type: " + packetData.getDataTypeName());

                //TODO cast PacketDataType into an actual datatype and change the current world state

                if(packetData.expectsAnswer()){
                    //TODO prepare answer for received packet
                }
            }else{
                System.out.println("Received data is not an instance of PacketDataType");
            }
        } catch (IOException e) {
            System.out.println("Error while receiving data: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Received data is not a class");
        }finally {
            try {
                currentSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
