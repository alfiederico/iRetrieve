/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.controller;

/**
 *
 * @author Alfie
 */
import com.iRetrieve.cloud.domain.Message;
import com.iRetrieve.cloud.domain.Report;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.iRetrieve.cloud.domain.User;
import com.iRetrieve.cloud.domain.VerificationToken;
import com.iRetrieve.cloud.service.ReportService;
import com.iRetrieve.cloud.service.UserService;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private Session emailSession;
    @Autowired
    private MessageSource messages;

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/mobile/login", method = RequestMethod.GET)
    public @ResponseBody
    Message sayHello(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) {
        User userExists = userService.findUserByEmail(username);
        if (userExists != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, userExists.getPassword())) {
                return new Message(userExists.getUserId(), "success");
            } else {
                return new Message(0, "fail");
            }
        } else {
            return new Message(0, "fail");
        }

    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        boolean bTokenExpired = false;
        if (userExists != null) {
            if (userExists.getActive() == true) {
                bindingResult
                        .rejectValue("email", "error.user",
                                "There is already a user registered with the email provided");
            } else {
                VerificationToken verificationToken = userService.getVerificationToken(userExists);
                Calendar cal = Calendar.getInstance();
                if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) > 0) {
                    bindingResult
                            .rejectValue("email", "error.user",
                                    "There is already a user registered with the email provided. Please check confirmation message sent to this email account.");
                } else {
                    bTokenExpired = true;
                }

            }

        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            try {
                String token = UUID.randomUUID().toString();
                if (bTokenExpired == true) {
                    userExists.setName(user.getName());
                    userExists.setLastName(user.getLastName());
                    userExists.setEmail(user.getEmail());
                    userExists.setPassword(user.getPassword());
                    userService.saveUser(userExists);
                    userService.createVerificationToken(userExists, token);
                } else {
                    userService.registerNewUserAccount(user);
                    userService.createVerificationToken(user, token);
                }
                modelAndView.addObject("successMessage", "You registered successfully. We will send you a confirmation message to your email account.");

                String recipientAddress = user.getEmail();
                String subject = "IRetrieve Registration: Email Confirmation";
                String confirmationUrl = "http://localhost:8089" + "/registrationConfirm?token=" + token;

                javax.mail.Message message = new MimeMessage(emailSession);
                message.setFrom(new InternetAddress("developer@alfiederico.com", "IRetrieve"));
                message.setRecipients(javax.mail.Message.RecipientType.TO,
                        InternetAddress.parse(recipientAddress));
                message.setSubject(subject);

                String content = "To complete and confirm your registration for IRerrieve, you’ll need to verify your email address. To do so, please click the link below: " + "\n\n" + confirmationUrl + "\n\n\n";
                content += "Best regards, \nalfred federico";
                message.setText(content);

                Transport.send(message);

                modelAndView.addObject("user", new User());
                modelAndView.setViewName("registration");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }
        return modelAndView;
    }

    @RequestMapping(value = "/mobile/registration", method = RequestMethod.POST)
    public @ResponseBody
    User addNewUser(@RequestBody User user) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            return null;
        } else {
            userService.saveUser(user);
            return user;
        }

    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage", "Instructions goes here");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", "invalid token");
            return "redirect:/badUser.html";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "redirect:/badUser.html";
        }
        if (user.getActive() == true) {
            return "redirect:/login";
        } else {
            user.setActive(true);
            userService.saveUser(user);
        }

        return "redirect:/login";
    }

}
