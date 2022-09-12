package prj.db;

import prj.world.World;
import prj.world.WorldState;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Map;

public class DBUtils {

    public static void saveState(String username, World world, EntityManager em){
        UserEntity ue = em.find(UserEntity.class, username);
        if(ue == null){
            ue = new UserEntity();
            ue.setUsername(username);
        }

        Map.Entry<Integer, WorldEntity> e = WorldEntity.save(ue, world.getState(), em);
        if(e.getKey() == -1){
            ue.getWorlds().add(e.getValue());
        }else {
            ue.getWorlds().set(e.getKey(), e.getValue());
        }

        em.merge(ue);
        em.flush();
    }

    public static WorldState loadState(String username, String worldName, EntityManager em){
        UserEntity ue = em.find(UserEntity.class, username);
        if(ue == null) return new WorldState();

        Query q = em.createQuery("from WorldEntity where user.username = :owner and worldname = :name").setParameter("owner", username).setParameter("name", worldName);

        WorldEntity we = null;
        try {
            we = (WorldEntity) q.getSingleResult();
        }catch (NoResultException ignored){}

        if(we == null) return new WorldState();

        return WorldEntity.load(we);
    }
}
