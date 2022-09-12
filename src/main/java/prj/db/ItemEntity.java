package prj.db;

import prj.item.Item;

import javax.persistence.*;

@Entity
@Table(name="items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public static ItemEntity export(Item i, EntityManager em){
        ItemEntity out = new ItemEntity();

        out.name = i.getItemName();

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
}
