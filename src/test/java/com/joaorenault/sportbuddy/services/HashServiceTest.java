package com.joaorenault.sportbuddy.services;

import java.security.NoSuchAlgorithmException;


class HashServiceTest {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = "johnny";

        HashService hashService = new HashService();
        String hashPass = hashService.hashPass(password);
        System.out.println("The hash for "+password+" is: "+hashPass);

        System.out.println(hashService.hashPass("jonny"));
    }

}