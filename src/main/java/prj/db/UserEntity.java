package prj.db;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users", schema = "pr3")
public class UserEntity {
    @Id
    private String username;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner")
    private List<WorldEntity> worlds = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<WorldEntity> getWorlds() {
        return worlds;
    }

    public void setWorlds(List<WorldEntity> worlds) {
        this.worlds = worlds;
    }
}
