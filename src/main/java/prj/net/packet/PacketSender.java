package prj.net.packet;

import prj.log.Logger;
import prj.net.packet.system.MultiPacket;
import prj.world.World;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PacketSender extends Thread {
    private final Logger logger = new Logger("");
    private final Socket sessionSocket;
    private final World localWorld;
    private final List<Packet> dataIn;

    public PacketSender(Socket sessionSocket, World localWorld, List<Packet> dataIn) {
        logger.setName(String.format("%s packet sender [%s]", (localWorld.isServerWorld() ? "Server" : "Client"), sessionSocket.getInetAddress()));
        this.sessionSocket = sessionSocket;
        this.localWorld = localWorld;
        this.dataIn = new ArrayList<>();

        this.dataIn.addAll(dataIn);
    }

    public PacketSender(Socket sessionSocket, World localWorld, Packet... dataIn) {
        this(sessionSocket, localWorld, List.of(dataIn));
    }

    @Override
    public void run() {
        logger.dbg("session started");
        try {
            int expectedPacketNum = 0;

            if(dataIn.size() > 1){
                Packet mp = new MultiPacket(dataIn.size());
                logger.dbg("multiple packets; sending additional packet: " + mp.getPacketName());
                Packet.send(sessionSocket, mp);
            }

            for(Packet p : dataIn){
                expectedPacketNum += p.expectedReturnPackets.size();
            }

            Packet.send(sessionSocket, dataIn);

            logger.dbg("awaiting " + expectedPacketNum + " packets");
            for (int i = 0; i < expectedPacketNum; i++) {
                Packet p = Packet.receive(sessionSocket);
                logger.announcePackets(null, String.format("(%d/%d)", (i+1), expectedPacketNum), p);
                localWorld.applyPacketData(p);
            }
        } catch (IOException e) {
            logger.err("sending packets failed: " + e.getMessage());
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
