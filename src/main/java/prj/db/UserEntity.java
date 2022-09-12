package prj.db;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="users")
public class UserEntity {
    @Id
    private String username;

    @ElementCollection
    @CollectionTable(name = "worlds", joinColumns = @JoinColumn(name = "id"))
    private Set<WorldEntity> worlds;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<WorldEntity> getWorlds() {
        return worlds;
    }

    public void setWorlds(Set<WorldEntity> worlds) {
        this.worlds = worlds;
    }
}
