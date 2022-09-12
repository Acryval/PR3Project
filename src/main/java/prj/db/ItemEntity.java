package prj.db;

import prj.entity.Player;
import prj.item.Block;
import prj.item.Item;
import prj.item.Pickaxe;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="items", schema = "pr3")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;

    @ManyToOne
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    private PlayerEntity player;

    private int slot;

    public static ItemEntity save(PlayerEntity p, Item i, int slot){
        ItemEntity out;

        List<ItemEntity> il = p.getItems().stream().filter(itemEntity -> itemEntity.slot == slot).toList();
        if(il.size() == 0){
            out = new ItemEntity();
            out.setSlot(slot);
        }else{
            out = il.get(0);
        }

        out.setType(i.getItemName());

        return out;
    }

    public static Item load(ItemEntity i){
        Item out = null;

        switch (i.type){
            case "Pickaxe" -> out = new Pickaxe();
            case "BlockPrototype" -> out = new Block();
        }

        return out;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public int getSlot() {
        return slot;
    }
}
