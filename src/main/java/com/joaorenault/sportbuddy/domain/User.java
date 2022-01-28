package com.joaorenault.sportbuddy.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "user") //deleting should be cascaded
    private LoginAccess login;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Use a valid email format")
    private String email;

    @ManyToMany
    private List<Match> participatingMatches = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> favouriteSports;

    public User() {
    }

    public User(LoginAccess login, String name, String email) {
        this.login = login;
        this.name = name;
        this.email = email;
    }
}
