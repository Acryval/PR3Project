package prj.db;

import prj.wall.DefaultBreakableWall;
import prj.wall.DefaultSpikeWall;
import prj.wall.Wall;

import javax.persistence.*;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "walls", schema = "pr3")
public class WallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "world_id", insertable = false, updatable = false)
    private WorldEntity world;

    private int xpos;
    private int ypos;
    private String type;

    public static Map.Entry<Integer, WallEntity> save(WorldEntity we, Wall w){
        WallEntity out;
        int indx = -1;

        List<WallEntity> wl = we.getWalls().stream().filter(wallEntity -> wallEntity.xpos == w.getX() && wallEntity.ypos == w.getY()).toList();
        if(wl.size() == 0){
            out = new WallEntity();
            out.setXpos(w.getX());
            out.setYpos(w.getY());
        }else{
            out = wl.get(0);
            indx = we.getWalls().indexOf(out);
        }

        out.setType(w.getType());

        return new AbstractMap.SimpleEntry<>(indx, out);
    }

    public static Wall load(WallEntity e){
        Wall out = null;

        switch (e.getType()){
            case "DefaultSpikeWall" -> out = new DefaultSpikeWall(e.xpos, e.ypos);
            case "DefaultBreakableWall" -> out = new DefaultBreakableWall(e.xpos, e.ypos);
        }

        return out;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public WorldEntity getWorld() {
        return world;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public String getType() {
        return type;
    }
}
