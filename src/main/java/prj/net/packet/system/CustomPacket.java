package prj.net.packet.system;

import prj.net.packet.Packet;
import prj.net.packet.PacketType;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CustomPacket extends Packet {
    @Override
    public PacketType getPacketType() {
        return PacketType.custom;
    }

    private final Class<? extends Packable> targetClass;
    private final Map<String, Serializable> customData;

    public CustomPacket(Packable p) {
        targetClass = p.getClass();
        customData = new HashMap<>();

        pack(p, "");
    }

    private void pack(Packable p, String namePrefix){
        for(Field f : p.getClass().getFields()){
            if(f.isAnnotationPresent(PacketElement.class)){
                f.setAccessible(true);
                String name = namePrefix + f.getName();
                try {
                    Object o = f.get(p);
                    if(o instanceof Packable pp){
                        pack(pp, name + ".");
                    }else if (o instanceof Serializable so) {
                        customData.put(name, so);
                    }
                }catch (IllegalAccessException e){
                    System.err.println("cannot get access to a field: " + name + ": " + e.getMessage());
                }
            }
        }
    }

    private void unpack(Packable p, String namePrefix){
        for(Field f : p.getClass().getFields()){
            if(f.isAnnotationPresent(PacketElement.class)){
                f.setAccessible(true);
                String name = namePrefix + f.getName();

                boolean complexObj = false;

                for(Map.Entry<String, Serializable> e : customData.entrySet()){
                    if(e.getKey().startsWith(name + ".")){
                        complexObj = true;
                        break;
                    }
                }

                try {
                    if(complexObj){
                        Packable pp = (Packable) f.getType().getConstructor().newInstance();
                        unpack(pp, name + ".");
                        f.set(p, pp);
                    }else{
                        f.set(p, customData.get(name));
                    }
                }catch (IllegalAccessException e){
                    System.err.println("Cannot unpack field: " + f.getName() + ": " + e.getMessage());
                } catch (InvocationTargetException | InstantiationException| NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void unpacInto(Packable p){
        if(p.getClass() != targetClass) {
            System.err.println("Cannot unpack " + targetClass.getName() + " into " + p.getClass().getName());
            return;
        }
        unpack(p, "");
    }
}
