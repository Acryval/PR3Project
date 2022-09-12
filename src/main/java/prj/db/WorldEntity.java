package prj.db;

import prj.world.WorldState;

import javax.persistence.*;
import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "worlds", schema = "pr3")
public class WorldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String worldname;

    @ManyToOne
    @JoinColumn(name = "owner", insertable = false, updatable = false)
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "world_id")
    private final List<WallEntity> walls = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "world_id")
    private final List<PlayerEntity> players = new ArrayList<>();

    public static Map.Entry<Integer, WorldEntity> save(UserEntity ue, WorldState state, EntityManager em){
        WorldEntity out;
        int indx = -1;

        List<WorldEntity> wl = ue.getWorlds().stream().filter(w -> w.worldname.equals(state.worldName)).toList();
        if(wl.size() == 0){
            out = new WorldEntity();
            out.setWorldname(state.worldName);
        }else{
            out = wl.get(0);
            indx = ue.getWorlds().indexOf(out);
        }

        state.players.forEach((k, v) -> {
            Map.Entry<Integer, PlayerEntity> e = PlayerEntity.save(out, v, k);
            if(e.getKey() == -1){
                out.getPlayers().add(e.getValue());
            }else{
                out.getPlayers().set(e.getKey(), e.getValue());
            }
        });

        List<WallEntity> toRemove = out.getWalls().stream().filter(wallEntity -> !state.wallsByCords.containsKey(new Point(wallEntity.getXpos(), wallEntity.getYpos()))).toList();
        toRemove.forEach(wallEntity -> {
            em.remove(wallEntity);
            out.getWalls().remove(wallEntity);
        });

        state.wallsByCords.forEach((k, v) -> {
            Map.Entry<Integer, WallEntity> e = WallEntity.save(out, v);
            if(e.getKey() == -1){
                out.getWalls().add(e.getValue());
            }else{
                out.getWalls().set(e.getKey(), e.getValue());
            }
        });

        return new AbstractMap.SimpleEntry<>(indx, out);
    }

    public static WorldState load(WorldEntity e){
        WorldState out = new WorldState();

        out.worldName = e.worldname;
        e.getPlayers().forEach(p -> out.players.put(p.getName(), PlayerEntity.load(p)));
        e.getWalls().forEach(w -> out.wallsByCords.put(new Point(w.getXpos(), w.getYpos()), WallEntity.load(w)));

        return out;
    }

    public void setWorldname(String worldname) {
        this.worldname = worldname;
    }

    public String getWorldname() {
        return worldname;
    }

    public UserEntity getUser() {
        return user;
    }

    public List<WallEntity> getWalls() {
        return walls;
    }

    public List<PlayerEntity> getPlayers() {
        return players;
    }
}
