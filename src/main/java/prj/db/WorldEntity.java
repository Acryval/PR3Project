package prj.db;

import prj.ClientThread;
import prj.world.WorldState;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "worlds")
public class WorldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany
    @JoinColumn(name = "username")
    private UserEntity user;

    @ElementCollection
    @CollectionTable(name = "players", joinColumns = @JoinColumn(name = "id"))
    private Set<PlayerEntity> players;

    @ElementCollection
    @CollectionTable(name = "walls", joinColumns = @JoinColumn(name = "id"))
    private Set<WallEntity> walls;

    public static WorldEntity export(WorldState state, UserEntity u, EntityManager em){
        WorldEntity out = new WorldEntity();

        out.name = state.worldName;
        out.user = u;
        state.players.forEach((k, v) -> out.players.add(PlayerEntity.export(v, k, out, em)));
        state.wallsByCords.forEach((k, v) -> out.walls.add(WallEntity.export(v, out, em)));

        em.merge(out);
        return out;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<PlayerEntity> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerEntity> players) {
        this.players = players;
    }

    public Set<WallEntity> getWalls() {
        return walls;
    }

    public void setWalls(Set<WallEntity> walls) {
        this.walls = walls;
    }
}
