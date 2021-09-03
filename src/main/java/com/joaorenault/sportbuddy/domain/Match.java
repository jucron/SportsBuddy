package com.joaorenault.sportbuddy.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @ManyToOne
    private User users;

    @NotBlank(message = "Date is mandatory")
    private String date;

    @NotBlank(message = "Hour is mandatory")
    private String hour;

    @NotBlank(message = "Location is mandatory")
    private String location;

    private String details;

    private Long ownerID;

    private int numberOfParticipants;

    private int sportChoice;

    @OneToOne
    private Sports sports;

    public Match(String name, String date, String hour, String location, String details, Long ownerID, Sports sports) {
        this.name = name;
        this.date = date;
        this.hour = hour;
        this.location = location;
        this.details = details;
        this.ownerID = ownerID;
        this.sports = sports;
    }

    public int getSportChoice() {
        return sportChoice;
    }

    public void setSportChoice(int sportChoice) {
        this.sportChoice = sportChoice;
    }

    public Match() {
    }

    public Sports getSports() {
        return sports;
    }

    public void setSports(Sports sports) {
        this.sports = sports;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }
}
