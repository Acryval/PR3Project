package prj.net.packet;

import java.io.Serializable;

public interface PacketDataType extends Serializable {
    String getDataTypeName();
    boolean expectsAnswer();
}
