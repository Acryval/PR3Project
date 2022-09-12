package prj.db;

import prj.wall.DefaultUnbreakableWall;
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

        state.wallsByCords.entrySet().stream().filter(e -> !e.getValue().getType().equals("DefaultUnbreakableWall")).forEach(v -> {
            Map.Entry<Integer, WallEntity> e = WallEntity.save(out, v.getValue());
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

        for(int i = -500 ; i < 0 ; i += 50) {
            for(int j = -3000 ; j < 4400 ; j += 50) {
                out.wallsByCords.put(new Point(j, i), new DefaultUnbreakableWall(j, i));
            }
        }
        for(int i = 0 ; i < 1500 ; i += 50) {
            for(int j = -3000 ; j < -2000 ; j += 50) {
                out.wallsByCords.put(new Point(j, i), new DefaultUnbreakableWall(j, i));
                out.wallsByCords.put(new Point(j + 6400, i), new DefaultUnbreakableWall(j + 6400, i));
            }
        }
        for(int i = 1500 ; i < 2000 ; i += 50) {
            for(int j = -3000 ; j < 4400 ; j += 50) {
                out.wallsByCords.put(new Point(j, i), new DefaultUnbreakableWall(j, i));
            }
        }

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
