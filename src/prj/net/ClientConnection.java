package prj.net;

import prj.net.packet.Packet;
import prj.net.packet.PacketDataType;
import prj.world.World;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ClientConnection <T extends PacketDataType> extends Thread {
    private final String serverHost;
    private final int serverPort;
    private final World localWorld;
    private final T dataIn;

    public ClientConnection(String serverHost, int serverPort, World localWorld, T dataIn) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.localWorld = localWorld;
        this.dataIn = dataIn;

        this.start();
    }

    @Override
    public void run() {
        Socket localSession = null;

        try {
            localSession = new Socket(serverHost, serverPort);

            Packet.send(localSession, dataIn);
            //TODO make logger
            System.out.println("[" + new Date(System.currentTimeMillis()) + "] " + "Packet of type " + dataIn.getDataTypeName() + " sent to " + localSession.getInetAddress());

            if(dataIn.expectsAnswer()){
                Object o = Packet.receive(localSession);

                if(o instanceof PacketDataType){
                    //TODO make logger
                    System.out.println("[" + new Date(System.currentTimeMillis()) + "] " + "Packet of type " + dataIn.getDataTypeName() + " received from " + localSession.getInetAddress());
                    localWorld.applyPacketData((PacketDataType) o);
                }else{
                    System.out.println("[" + new Date(System.currentTimeMillis()) + "] " + "Data received from " + localSession.getInetAddress() + " is not an instance of PacketDataType");
                }
            }

            localSession.close();
        } catch (IOException e) {
            System.out.println("Error while processing packet: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Received data is not a class");
        }finally {
            if(localSession != null){
                try{
                    localSession.close();
                }catch (IOException e){
                    System.out.println("Error closing socket");
                }
            }
        }
    }
}
