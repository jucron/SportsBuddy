package com.joaorenault.sportbuddy.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class MailServiceImplTest {
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private MimeMessage mail;
    @InjectMocks
    private MailServiceImpl mailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mailService =  new MailServiceImpl(mailSender);

    }
    @Test
    void passRecovery() throws MessagingException {
        //given
        String email = "email-test";
        String username = "username-test";

        given(mailSender.createMimeMessage()).willReturn(mail);

        //when
        String newRandomPass = mailService.passRecovery(email,username);

        //then
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
        Assertions.assertEquals(newRandomPass.getClass(),String.class);
    }
}