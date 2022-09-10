package prj.entity;

import prj.item.ItemBar;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_u")
    private int id;

    @Column(name="username")
    private String username;

    //Trzeba jakoś wziąć itemy z playera
    private ItemBar itemBar;
}
