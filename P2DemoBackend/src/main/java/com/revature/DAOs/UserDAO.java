package com.revature.DAOs;

import com.revature.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

//Extending JpaRepository gives us access to a bunch of basic CRUD methods. We don't have to write them!
    //This includes find all, find by id, insert, update, delete

//JpaRepository takes 2 generics:
    //1) The entity we're working with (User in this case)
    //2) The type of the primary key (Integer in this case)
@Repository //1 of the 4 stereotype annotations (make a class a bean)
public interface UserDAO extends JpaRepository<User, UUID> {

    //For login, we can check if an inpuuted username/password matches a record in the DB
    //If it returns null, the user doesn't exist and login fails
    //If it returns a User object, the user exists and login succeeds
    public Optional<User> findByUsernameAndPassword(String username, String password);

    //We're using this in WebSecurityConfig to authenticate users
    public User findByUsername(String username);

}
