package com.joaorenault.sportbuddy.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotNull
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotNull
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull
    @NotBlank(message = "Email is mandatory")
    private String email;

    @ManyToMany
    private List<Match> participatingMatches = new ArrayList<>();

    public User() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Match> getParticipatingMatches() {
        return participatingMatches;
    }

    public void setParticipatingMatches(List<Match> participatingMatches) {
        this.participatingMatches = participatingMatches;
    }

}
