package com.joaorenault.sportbuddy.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @ManyToMany
    private List<User> usersAttending = new ArrayList<>();

    //    @Min(value = 9,message = "Complete the Date")
    @NotBlank(message = "Date is mandatory")
    private String date;

    //    @Min(value = 4,message = "Complete the Hour")
    @NotBlank(message = "Hour is mandatory")
    private String hour;

    @NotBlank(message = "Location is mandatory")
    private String location;

    private String details;

    private Long ownerID;

    private String ownerName;

    private int numberOfParticipants;

    private int sportChoice;

    private String sportName;

    public Match() {
    }

    public Match(Long id, String name, List<User> usersAttending, String date, String hour, String location, String details, Long ownerID, String ownerName, int numberOfParticipants, int sportChoice, String sportName) {
        this.id = id;
        this.name = name;
        this.usersAttending = usersAttending;
        this.date = date;
        this.hour = hour;
        this.location = location;
        this.details = details;
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.numberOfParticipants = numberOfParticipants;
        this.sportChoice = sportChoice;
        this.sportName = sportName;
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

    public List<User> getUsersAttending() {
        return usersAttending;
    }

    public void setUsersAttending(List<User> usersAttending) {
        this.usersAttending = usersAttending;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public int getSportChoice() {
        return sportChoice;
    }

    public void setSportChoice(int sportChoice) {
        this.sportChoice = sportChoice;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
}
