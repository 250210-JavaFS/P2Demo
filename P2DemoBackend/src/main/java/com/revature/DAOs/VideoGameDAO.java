package com.revature.DAOs;

import com.revature.models.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository //Make this Interface a bean
public interface VideoGameDAO extends JpaRepository<VideoGame, UUID> {

    //Find a list of games by their User's id
    public List<VideoGame> findByUser_UserId(UUID userId);

    //Why User_UserId?
    //We're digging into the User object in the VideoGame object
        //...in order to access the primary key field of User (userId)

}
