package com.joaorenault.sportbuddy.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private LoginAccess login;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Use a valid email format")
    private String email;

    @ManyToMany
    private List<Match> participatingMatches = new ArrayList<>();

    public User() {
    }

    public User(LoginAccess login, String name, String email) {
        this.login = login;
        this.name = name;
        this.email = email;
    }

    public LoginAccess getLogin() {
        return this.login;
    }

    public void setLogin(LoginAccess login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

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
