package prj.world;

public enum Direction{
    UP("up"),
    DOWN("down"),
    LEFT("left"),
    RIGHT("right");

    private final String name;

    Direction(String n){
        name = n;
    }

    public String getName(){
        return name;
    }
}
