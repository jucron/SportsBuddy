package com.joaorenault.sportbuddy.domain;

import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;


public class Login {

    @NotNull
    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotNull
    @NotBlank(message = "Password is mandatory")
    private String password;

    public Login() {
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
}
