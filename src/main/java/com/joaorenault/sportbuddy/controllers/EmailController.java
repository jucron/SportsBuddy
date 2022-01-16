package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.helper.FeedbackMessage;
import com.joaorenault.sportbuddy.services.LoginAccessService;
import com.joaorenault.sportbuddy.services.MailService;
import com.joaorenault.sportbuddy.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Controller
@Slf4j
public class EmailController {

    private final MailService mailService;
    private final LoginAccessService loginAccessService;
    private final UserService userService;

    public EmailController(MailService mailService, LoginAccessService loginAccessService, UserService userService) {
        this.mailService = mailService;
        this.loginAccessService = loginAccessService;
        this.userService = userService;
    }

    @GetMapping("pass-recovery") // "Entering Password recovery template"
    public String passRecoveryPage (Model model) {
        log.info("pass-recovery mapping accessed");
        model.addAttribute("emailForm", new EmailForm());
        model.addAttribute("account", new FeedbackMessage(false));

        return "passrecovery";
    }

    @PostMapping("send_email")
    public String sendMail(@Valid @ModelAttribute("emailForm") EmailForm emailForm, BindingResult resultUser, Model model) {
        log.info("send_mail mapping accessed");
        if (resultUser.hasErrors()) { //checking field validation
            log.info("field user have errors. email = "+emailForm.getEmail());
            model.addAttribute("account", new FeedbackMessage(
                    true,1,"Email not valid."));
            resultUser.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            return "passrecovery";
        }
        //check if email exists
        User existingUser = userService.findUserByEmail(emailForm.getEmail());
        LoginAccess loginFound  = loginAccessService.findLoginByUser(existingUser);
        if (loginFound==null) {
            log.info("Email does not exists");
            model.addAttribute("account", new FeedbackMessage(
                    true,1,"Email was not found in database."));
            return "passrecovery";
        }
        //send details to email

        try {
            String newPass = mailService.passRecovery(emailForm.getEmail(),loginFound.getUsername());
            log.info("Email sent with new password");
            model.addAttribute("account", new FeedbackMessage(
                    true,2,"Your credentials were sent to your email address."));
            loginFound.setPassword(newPass); //setting the new pass
            loginAccessService.encodePassAndSaveLogin(loginFound); //saving the new login with the new pass

            return "passrecovery";
        } catch (Exception e) {

            log.info("Error sending email: "+e.getMessage());
            return "passrecovery";
        }


    }
}
class EmailForm {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Use a valid email format")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}