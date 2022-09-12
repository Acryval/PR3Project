package prj.db;

import prj.entity.Player;
import prj.item.Item;
import prj.item.ItemBar;
import prj.world.WorldState;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "players", schema = "pr3")
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "world_id", insertable = false, updatable = false)
    private WorldEntity world;

    private String name;
    private int xpos;
    private int ypos;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private final List<ItemEntity> items = new ArrayList<>();

    public static Map.Entry<Integer, PlayerEntity> save(WorldEntity we, Player p, String name){
        PlayerEntity out;
        int indx = -1;

        List<PlayerEntity> pl = we.getPlayers().stream().filter(playerEntity -> playerEntity.name.equals(name)).toList();
        if(pl.size() == 0){
            out = new PlayerEntity();
            out.setName(name);
        }else{
            out = pl.get(0);
            indx = we.getPlayers().indexOf(out);
        }

        out.setXpos(p.getX());
        out.setYpos(p.getY());

        for(int i = 0; i < p.getItemBar().getItemBar().size(); i++){
            Item it = p.getItemBar().getItemBar().get(i);
            if(it == null) continue;
            out.getItems().add(ItemEntity.save(out, it, i));
        }

        return new AbstractMap.SimpleEntry<>(indx, out);
    }

    public static Player load(PlayerEntity e){
        Player p = new Player(e.xpos, e.ypos, new ItemBar(10, 10, (1400 - 50) / 2, (900 - 100) / 2, 10, 50, 50, 10, 0));

        e.getItems().forEach(ie -> p.getItemBar().addItem(ie.getSlot(), ItemEntity.load(ie)));

        return p;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public WorldEntity getWorld() {
        return world;
    }

    public List<ItemEntity> getItems() {
        return items;
    }
}
