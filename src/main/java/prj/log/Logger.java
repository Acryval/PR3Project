package prj.log;

import prj.Prj;
import prj.net.packet.Packet;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;

public class Logger {
    private String name;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");

    public Logger(String name){
        this.name = name;
    }

    public Logger setName(String name) {
        this.name = name;
        return this;
    }

    public void log(LogType type, String s){
        String msg = String.format("%s [%s]:%s - %s", dateFormatter.format(new Date()), type.name, name, s);

        if(Prj.LOGFILE == null){
            if(type == LogType.DEBUG){
                if(Prj.DEBUG){
                    System.out.println(msg);
                }
            }else if(type == LogType.ERROR || type == LogType.WARNING){
                System.err.println(msg);
            }else{
                System.out.println(msg);
            }
        }else{
            try {
                FileWriter w = new FileWriter(Prj.LOGFILE, true);
                w.append(msg).append(System.lineSeparator());
                w.close();
            }catch (IOException e){
                System.err.println("File " + Prj.LOGFILE + " inaccessible");
            }
        }
    }

    public void out(String s){
        log(LogType.INFO, s);
    }

    public void warn(String s){
        log(LogType.WARNING, s);
    }

    public void err(String s){
        log(LogType.ERROR, s);
    }

    public void dbg(String s){
        log(LogType.DEBUG, s);
    }

    public void testinfo(String s){log(LogType.TEST, s);}

    public void announcePackets(InetSocketAddress address, String prefix, List<Packet> packets){
        StringBuilder s = new StringBuilder(prefix).append(" {");

        if(packets != null){
            if(packets.size() == 1){
                s.append(packets.get(0).getPacketName());
            }else {
                for (Packet p : packets) {
                    s.append("\n\t").append(p.getPacketName());
                }
                s.append("\n");
            }
        }else{
            s.append("[CORRUPTED]");
        }

        s.append("}");

        if(address != null){
            s.append(" to ").append(address);
        }

        log(LogType.PACKET, s.toString());
    }

    public void announcePackets(InetSocketAddress address, String prefix, Packet... packets){
        announcePackets(address, prefix, List.of(packets));
    }
}
