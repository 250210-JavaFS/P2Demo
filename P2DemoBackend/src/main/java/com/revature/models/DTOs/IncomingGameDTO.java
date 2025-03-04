package com.revature.models.DTOs;

//Here's another DTO - but we aren't leaving out a password or anything
//This time we want to make a cleaner request body when inserting a new game

//We don't want to have to insert an entire user object to insert a game
//What is the game has like 10 objects it depends on? that's gonna be ugly JSON

//SOLUTION: just pass in the User's ID instead of the whole object

//Side note: we'll also leave out gameId since the DB handles that

import java.util.UUID;

public class IncomingGameDTO {

    private String title;
    private String genre;
    private UUID userId;

    //boilerplate------------------

    public IncomingGameDTO() {
    }

    public IncomingGameDTO(String title, String genre, UUID userId) {
        this.title = title;
        this.genre = genre;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "IncomingGameDTO{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", userId=" + userId +
                '}';
    }
}
