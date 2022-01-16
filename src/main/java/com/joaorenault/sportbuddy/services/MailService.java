package com.joaorenault.sportbuddy.services;

import javax.mail.MessagingException;

public interface MailService {
    String passRecovery (String email, String username) throws MessagingException;
}
