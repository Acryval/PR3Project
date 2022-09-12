package prj.db;

import prj.wall.Wall;

import javax.persistence.*;

@Entity
@Table(name = "walls")
public class WallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinColumn(name = "id")
    private WorldEntity world;

    private int xpos;
    private int ypos;
    private String type;

    public static WallEntity export(Wall w, WorldEntity world, EntityManager em){
        WallEntity out = new WallEntity();

        out.world = world;
        out.xpos = w.getX();
        out.ypos = w.getY();
        out.type = w.getType();

        em.merge(out);
        return out;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WorldEntity getWorld() {
        return world;
    }

    public void setWorld(WorldEntity world) {
        this.world = world;
    }

    public int getXpos() {
        return xpos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
