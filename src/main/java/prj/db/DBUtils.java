package prj.db;

import prj.world.WorldState;

import javax.persistence.EntityManager;

public class DBUtils {

    public static void saveProgress(String username, WorldState worldState, EntityManager em){
        UserEntity eu = em.find(UserEntity.class, username);
        if(eu == null){
            eu = new UserEntity();
            eu.setUsername(username);
        }

        eu.getWorlds().add(WorldEntity.export(worldState, eu, em));
        em.merge(eu);
    }
}
