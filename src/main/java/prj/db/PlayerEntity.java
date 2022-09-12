package prj.db;

import prj.entity.Player;

import javax.persistence.*;

@Entity
@Table(name = "players")
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinColumn(name = "id")
    private WorldEntity world;
    private String name;

    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item0;
    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item1;
    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item2;
    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item3;
    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item4;
    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item5;
    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item6;
    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item7;
    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item8;
    @ManyToOne
    @JoinColumn(name = "id")
    private ItemEntity item9;

    public static PlayerEntity export(Player p, String username, WorldEntity world, EntityManager em){
        PlayerEntity out = new PlayerEntity();

        out.world = world;
        out.name = username;
        out.item0 = ItemEntity.export(p.getItemBar().getItemBar().get(0), em);
        out.item1 = ItemEntity.export(p.getItemBar().getItemBar().get(1), em);
        out.item2 = ItemEntity.export(p.getItemBar().getItemBar().get(2), em);
        out.item3 = ItemEntity.export(p.getItemBar().getItemBar().get(3), em);
        out.item4 = ItemEntity.export(p.getItemBar().getItemBar().get(4), em);
        out.item5 = ItemEntity.export(p.getItemBar().getItemBar().get(5), em);
        out.item6 = ItemEntity.export(p.getItemBar().getItemBar().get(6), em);
        out.item7 = ItemEntity.export(p.getItemBar().getItemBar().get(7), em);
        out.item8 = ItemEntity.export(p.getItemBar().getItemBar().get(8), em);
        out.item9 = ItemEntity.export(p.getItemBar().getItemBar().get(9), em);

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

    public ItemEntity getItem0() {
        return item0;
    }

    public void setItem0(ItemEntity item0) {
        this.item0 = item0;
    }

    public ItemEntity getItem1() {
        return item1;
    }

    public void setItem1(ItemEntity item1) {
        this.item1 = item1;
    }

    public ItemEntity getItem2() {
        return item2;
    }

    public void setItem2(ItemEntity item2) {
        this.item2 = item2;
    }

    public ItemEntity getItem3() {
        return item3;
    }

    public void setItem3(ItemEntity item3) {
        this.item3 = item3;
    }

    public ItemEntity getItem4() {
        return item4;
    }

    public void setItem4(ItemEntity item4) {
        this.item4 = item4;
    }

    public ItemEntity getItem5() {
        return item5;
    }

    public void setItem5(ItemEntity item5) {
        this.item5 = item5;
    }

    public ItemEntity getItem6() {
        return item6;
    }

    public void setItem6(ItemEntity item6) {
        this.item6 = item6;
    }

    public ItemEntity getItem7() {
        return item7;
    }

    public void setItem7(ItemEntity item7) {
        this.item7 = item7;
    }

    public ItemEntity getItem8() {
        return item8;
    }

    public void setItem8(ItemEntity item8) {
        this.item8 = item8;
    }

    public ItemEntity getItem9() {
        return item9;
    }

    public void setItem9(ItemEntity item9) {
        this.item9 = item9;
    }
}
