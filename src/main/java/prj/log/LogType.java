package prj.log;

public enum LogType {
    INFO("INFO"),
    PACKET("PACK"),
    WARNING("WARN"),
    ERROR("ERR"),
    DEBUG("DEBG"),
    TEST("TEST");

    public final String name;

    LogType(String name){
        this.name = name;
    }
}
