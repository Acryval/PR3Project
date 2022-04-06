package prj.net.packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Packet<T extends PacketDataType> extends Thread{
    private final String serverHost;
    private final int serverPort;
    private final T dataIn;
    private Object dataOut;

    public Packet(String serverHost, int serverPort, T dataIn) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.dataIn = dataIn;
        this.dataOut = null;

        this.start();
    }

    @Override
    public void run() {
        try {
            Socket session = new Socket(serverHost, serverPort);

            send(session, dataIn);
            if(dataIn.expectsAnswer()) dataOut = receive(session);

            session.close();
        } catch (IOException e) {
            //TODO make logger
            System.out.println("Failed to send packet of type " + dataIn.getDataTypeName() + " to host " + serverHost + ", port " + serverPort);
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Received data is not valid");
            dataOut = null;
        }
    }

    public Object getPossibleData() {
        return dataOut;
    }

    public static <T extends PacketDataType> void send(Socket session, T data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(session.getOutputStream());
        out.writeObject(data);
        out.flush();
        out.close();
    }

    public static Object receive(Socket session) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(session.getInputStream());
        Object data = in.readObject();
        in.close();
        return data;
    }
}
