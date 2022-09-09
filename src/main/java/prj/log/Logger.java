package prj.log;

import prj.Prj;
import prj.net.packet.Packet;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
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

    public static String getFormattedMsg(LogType type, String name, String msg){
        return String.format("%s [%s]:%s - %s", dateFormatter.format(new Date()), type, name, msg);
    }

    public void log(LogType type, String s){
        if(type == LogType.PACK && !Prj.LOG_PACKETS) return;

        String msg = getFormattedMsg(type, name, s);

        if(Prj.LOG_INFO){
            if(Prj.DEBUG || type != LogType.DEBG) {
                try {
                    FileWriter w = new FileWriter(Prj.LOGFILE, true);
                    w.append(msg).append(System.lineSeparator());
                    w.flush();
                    w.close();
                } catch (IOException e) {
                    System.err.println("File " + Prj.LOGFILE + " inaccessible");
                }
            }
        }else{
            if(type == LogType.DEBG){
                if(Prj.DEBUG){
                    System.out.println(msg);
                }
            }else if(type == LogType.ERR || type == LogType.WARN){
                System.err.println(msg);
            }else{
                System.out.println(msg);
            }
        }
    }

    public void out(String s){
        log(LogType.INFO, s);
    }

    public void warn(String s){
        log(LogType.WARN, s);
    }

    public void err(String s){
        log(LogType.ERR, s);
    }

    public void dbg(String s){
        log(LogType.DEBG, s);
    }

    public void testinfo(String s){log(LogType.TEST, s);}

    public void announcePackets(InetAddress address, String prefix, List<Packet> packets){
        if(!Prj.LOG_INFO) return;
        StringBuilder s = new StringBuilder(prefix).append(" {");

        if(packets != null){
            if(packets.size() == 1){
                s.append(packets.get(0).getName());
            }else {
                for (Packet p : packets) {
                    s.append("\n\t").append(p.getName());
                }
                s.append("\n");
            }
        }else{
            s.append("[INVALID]");
        }

        s.append("}");

        if(address != null){
            s.append(" to ").append(address);
        }

        log(LogType.PACK, s.toString());
    }

    public void announcePackets(InetAddress address, String prefix, Packet... packets){
        announcePackets(address, prefix, List.of(packets));
    }
}
