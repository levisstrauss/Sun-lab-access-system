package com.levisstrauss.sunlabaccesssystem.Objects;

import java.time.LocalDate;
import java.time.LocalTime;

public class User {
    private String id;
    private String userType;
    private String action;
    private LocalDate date;
    private  LocalTime time;


    public User(String id, String userType, String action, LocalDate date, LocalTime time) {
        this.id = id;
        this.userType = userType;
        this.action = action;
        this.date = date;
        this.time = time;
    }

    public User() {

    }

    public String getId() {
        return id;
    }


    public String getUserType() {
        return userType;
    }

    public String getAction() {
        return action;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

}