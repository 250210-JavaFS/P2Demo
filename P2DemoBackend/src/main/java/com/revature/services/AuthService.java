package com.revature.services;

import com.revature.DAOs.UserDAO;
import com.revature.models.DTOs.LoginDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//The Service layer is where we put our business logic
//User input validation, Data manipulation/reformatting, User authentication, etc.
@Service //1 of 4 stereotype annotations (make a class a bean)
public class AuthService {

    //Services talk to DAOs, so let's autowire the UserDAO so we can use its methods
    private final UserDAO userDAO;

    //We also need to autowire the password encoder to encrypt passwords
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserDAO userDAO, PasswordEncoder passwordEncoder){
        this.userDAO = userDAO;
        //Spring will create an instantiation of this DAO for us
        this.passwordEncoder = passwordEncoder;
    }

    //This method will take a User object and send it to the DAO
    //It will also return the inserted User to the Controller
    public OutgoingUserDTO registerUser(User user){

        //PASSWORD ENCRYPTION---
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //We use the save() method to insert data into the DB
        //save() is one of the methods we inherited from JpaRepository
        User returnedUser = userDAO.save(user); //save() returns the inserted data. Convenient!

        //We need to convert the User to a UserDTO before we send it to the client
        OutgoingUserDTO outUserDTO = new OutgoingUserDTO(
                returnedUser.getUserId(),
                returnedUser.getUsername(),
                returnedUser.getRole()
        );

        return outUserDTO;

    }

    //Login - takes a LoginDTO and uses those fields to try to get a User from the DAO
    public OutgoingUserDTO login(LoginDTO loginDTO){

        //input validation (actually doing it this time!)

        //check if the username is null, or if it is only an empty string or space-only string
        if(loginDTO.getUsername() == null || loginDTO.getUsername().isBlank()){
            throw new IllegalArgumentException("Username cannot be empty!");
        }

        //same for password
        if(loginDTO.getPassword() == null || loginDTO.getPassword().isBlank()){
            throw new IllegalArgumentException("Password cannot be empty!");
        }

        //TODO: could do more checks, but this is the gist

        //Try to get a User from the DAO
        User returnedUser = userDAO.findByUsernameAndPassword(
                loginDTO.getUsername(),
                loginDTO.getPassword()).orElse(null);

        //orElse(null) is a convenient way to extract data (or a null value) from an Optional
        //we could also use orElseThrow() to throw an Exception outright, but I'll spell it out a bit

        //If no User is found (if returnedUser is null) throw an Exception
        if(returnedUser == null){
            throw new IllegalArgumentException("Invalid username or password!");
        }

        //If we get here, login was successful so return a User to the controller
        return new OutgoingUserDTO(returnedUser); //using our convenient constructor that takes in a User
    }

}
