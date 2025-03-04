package com.revature.models;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component //1 of 4 stereotype annotations (make a class a bean)
@Entity //This makes the class a DB entity
@Table(name = "users") //This annotation lets us specify the name of the DB table
public class User {

    @Id //This annotation makes this field the PK in the DB table
    @GeneratedValue(strategy = GenerationType.UUID) //This annotation makes the PK auto-increment
    private UUID userId;

    //We don't need to specify @Column UNLESS we want to define a name, or constraints

    @Column(nullable = false) //so now every User needs a username
    //TODO: this should probably be unique
    private String username;

    private String password;

    private String role = "user"; //we set a default value for role
    //but we can specify a different role when we create a User if we need to

    //boilerplate-----------------------------------------------

    //right click -> generate -> choose the boilerplate you want

    public User() {
    }

    public User(UUID userId, String password, String role, String username) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
