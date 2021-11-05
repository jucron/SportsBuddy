package com.joaorenault.sportbuddy.services;

import java.security.NoSuchAlgorithmException;

public interface HashService {

    public String hashPass (String password) throws NoSuchAlgorithmException;
}
