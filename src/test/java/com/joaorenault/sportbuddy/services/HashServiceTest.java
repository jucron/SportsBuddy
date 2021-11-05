package com.joaorenault.sportbuddy.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;


class HashServiceTest {

    HashService hashService;

    @BeforeEach
    void setUp() {

        hashService = new HashServiceImpl();
    }

    @Test
    void encryptPassword() throws NoSuchAlgorithmException {
        String password = "johnny";
        String encryptedPass = hashService.hashPass(password);

        assertNotNull(encryptedPass);
        assertTrue(encryptedPass.length()>password.length());
        assertNotEquals(password,encryptedPass);
    }
    @Test
    void twoEncryptionAreTheSame() throws NoSuchAlgorithmException {
        String passwordAccess = "johnny";
        String encryptedPassAccess = hashService.hashPass(passwordAccess);
        String passwordRetrieve = "johnny";
        String encryptedPassRetrieve = hashService.hashPass(passwordRetrieve);

        assertNotNull(encryptedPassAccess);
        assertNotNull(encryptedPassRetrieve);
        assertEquals(passwordAccess,passwordRetrieve);
        assertEquals(encryptedPassAccess,encryptedPassRetrieve);
        assertNotEquals(passwordAccess,encryptedPassAccess);
    }
    @Test
    void wrongPasswordInput() throws  NoSuchAlgorithmException {
        String passwordAccess = "johnny";
        String encryptedPassAccess = hashService.hashPass(passwordAccess);
        String passwordRetrieve = "larry";
        String encryptedPassRetrieve = hashService.hashPass(passwordRetrieve);

        assertNotNull(encryptedPassAccess);
        assertNotNull(encryptedPassRetrieve);
        assertNotEquals(passwordAccess,passwordRetrieve);
        assertNotEquals(encryptedPassAccess,encryptedPassRetrieve);
        assertNotEquals(passwordAccess,encryptedPassAccess);
    }
}