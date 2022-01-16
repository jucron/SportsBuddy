package com.joaorenault.sportbuddy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String passRecovery(String email, String username) throws MessagingException {
        Random rand = new Random();
        int newRandomPass = rand.nextInt(100000);

        MimeMessage mail = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mail);
        helper.setTo(email);
        helper.setSubject( "Sports Buddy password recovery" );
        helper.setText("<h1>Greetings from Sports Buddy!</h1> " +
                        "<h2>Below you can find your username, with a new password to login:</h2>"+
                        "<p>Username: "+username+"</p>"+
                        "<p>Password: "+newRandomPass+"</p>"+
                        "<p>We recommend changing your password</p>",
                true);
        mailSender.send(mail);
        return Integer.toString(newRandomPass);
    }
}
