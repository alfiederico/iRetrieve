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
import com.iRetrieve.cloud.configuration.GeneratePdfReport;
import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Hotspot;
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
import com.iRetrieve.cloud.service.HistoryService;
import com.iRetrieve.cloud.service.HotspotService;
import com.iRetrieve.cloud.service.ReportService;
import com.iRetrieve.cloud.service.UserService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
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
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HotspotService hotspotService;

    @Autowired
    private Session emailSession;
    @Autowired
    private MessageSource messages;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/mobile/users", method = RequestMethod.GET)
    public @ResponseBody
    List<User> getPerson() {
        return userService.findAllByOrderByUserIdAsc();
        //return personService.getAllPerson();
    }

    @RequestMapping(value = {"/access-denied"}, method = RequestMethod.GET)
    public ModelAndView denied() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("denied");
        return modelAndView;
    }

    @RequestMapping(value = {"/confirm"}, method = RequestMethod.GET)
    public ModelAndView loginconfirm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("confirm");
        return modelAndView;
    }

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/mobile/login", method = RequestMethod.GET)
    public @ResponseBody
    Message sayHello(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) {
        User userExists = userService.findUserByEmail(username);
        if (userExists != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (userExists.getActive() == false) {
                return new Message(0, "activate", 0);
            }
            if (encoder.matches(password, userExists.getPassword())) {
                return new Message(userExists.getUserId(), "success", userExists.getRadius());
            } else {
                return new Message(0, "fail", 0);
            }
        } else {
            return new Message(0, "fail", 0);
        }

    }

    @RequestMapping(value = "/mobile/forgotpassword", method = RequestMethod.GET)
    public @ResponseBody
    Message sayPassword(@RequestParam(value = "email", required = true) String email) {
        User userExists = userService.findUserByEmail(email);
        if (userExists != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            try {
                String recipientAddress = userExists.getEmail();
                String subject = "IRetrieve : Forgot Password";

                javax.mail.Message message = new MimeMessage(emailSession);
                message.setFrom(new InternetAddress("developer@alfiederico.com", "IRetrieve"));
                message.setRecipients(javax.mail.Message.RecipientType.TO,
                        InternetAddress.parse(recipientAddress));
                message.setSubject(subject);

                String randompassword = getSaltString();

                String content = "Hi,\n" + userExists.getEmail() + "\n\nAre you trying to sign in?\nif so, use this code to finish signing in.\n" + randompassword + "\n\n\n";
                content += "Best regards, \nalfred federico";
                message.setText(content);

                Transport.send(message);

                userExists.setPassword(encoder.encode(randompassword));

                userService.saveUser(userExists);

                return new Message(0, "success", 0);

            } catch (Exception ex) {
                return new Message(0, ex.getMessage(), 0);
            }

        } else {
            return new Message(0, "fail", 0);
        }

    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

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
                    userExists.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                    userService.saveUser(userExists);
                    userService.createVerificationToken(userExists, token);
                } else {
                    user.setToken(0);
                    user.setPoints(0);
                    userService.registerNewUserAccount(user);
                    userService.createVerificationToken(user, token);
                }
                modelAndView.addObject("successMessage", "You registered successfully. We will send you a confirmation message to your email account.");

                String recipientAddress = user.getEmail();
                String subject = "IRetrieve Registration: Email Confirmation";
                String confirmationUrl = "http://alfiederico.com/iRetrieve-0.0.1" + "/registrationConfirm?token=" + token;

                javax.mail.Message message = new MimeMessage(emailSession);
                message.setFrom(new InternetAddress("developer@alfiederico.com", "IRetrieve"));
                message.setRecipients(javax.mail.Message.RecipientType.TO,
                        InternetAddress.parse(recipientAddress));
                message.setSubject(subject);

                String content = "To complete and confirm your registration for IRetrieve, you will need to verify your email address. To do so, please click the link below: " + "<br /><br /><a href=\"" + confirmationUrl + "\">Confirmed</a><br /><br /><br />";
                content += "Best regards, <br />alfred federico";

                message.setContent(content, "text/html");

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

        boolean bTokenExpired = false;

        if (userExists != null && userExists.getActive() == true) {
            return null;
        } else {

            if (userExists != null) {
                VerificationToken verificationToken = userService.getVerificationToken(userExists);
                Calendar cal = Calendar.getInstance();
                if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) > 0) {
                    User userverify = new User();
                    userverify.setName("Verify");
                    userverify.setLastName("");
                    return userverify;
                } else {
                    bTokenExpired = true;
                }
            }

        }

        try {
            String token = UUID.randomUUID().toString();
            if (bTokenExpired == true) {
                userExists.setName(user.getName());
                userExists.setLastName(user.getLastName());
                userExists.setEmail(user.getEmail());
                userExists.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                userService.saveUser(userExists);
                userService.createVerificationToken(userExists, token);
            } else {
                userService.registerNewUserAccount(user);
                userService.createVerificationToken(user, token);
            }

            String recipientAddress = user.getEmail();
            String subject = "IRetrieve Registration: Email Confirmation";
            String confirmationUrl = "http://alfiederico.com/iRetrieve-0.0.1" + "/registrationConfirm?token=" + token;

            javax.mail.Message message = new MimeMessage(emailSession);
            message.setFrom(new InternetAddress("developer@alfiederico.com", "IRetrieve"));
            message.setRecipients(javax.mail.Message.RecipientType.TO,
                    InternetAddress.parse(recipientAddress));
            message.setSubject(subject);

            String content = "To complete and confirm your registration for IRetrieve, you’ll need to verify your email address. To do so, please click the link below: " + "\n\n" + confirmationUrl + "\n\n\n";
            content += "Best regards, \nalfred federico";
            message.setText(content);

            Transport.send(message);

            return user;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public ModelAndView home(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        if (model.asMap().get("adminMessage") == null) {
            modelAndView.addObject("adminMessage", "Instructions goes here");
        }
        modelAndView.setViewName("admin/home");
        model.addAttribute("users", userService.findAllByOrderByUserIdAsc());
        model.addAttribute("reports", reportService.findAllByOrderByUserIdAsc());
        model.addAttribute("histories", historyService.findAllByOrderByUserIdAsc());
        model.addAttribute("hotspots", hotspotService.findAllByOrderByIdAsc());

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
            return "redirect:/confirm";
        } else {
            user.setActive(true);
            user.setRadius(50);
            userService.saveUser(user);
        }

        return "redirect:/confirm";
    }

    @RequestMapping(value = "/mobile/registrationConfirm", method = RequestMethod.GET)
    public @ResponseBody
    Message mobileConfirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return new Message(0, "Invalid token", 0);
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new Message(0, "Token Expired", 0);
        }
        if (user.getActive() == true) {
            return new Message(0, "User already active", 0);
        } else {
            user.setActive(true);
            user.setRadius(50);
            userService.saveUser(user);
            return new Message(0, "Account activation successful", 0);
        }

    }

    @RequestMapping(value = "/userreport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> usersReport() throws IOException {
        List<User> users = (List<User>) userService.findAllByOrderByUserIdAsc();

        ByteArrayInputStream bis = GeneratePdfReport.usersReport(users);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=User.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @RequestMapping(value = "/reportreport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> reportReport() throws IOException {
        List<Report> report = (List<Report>) reportService.findAllByOrderByUserIdAsc();

        ByteArrayInputStream bis = GeneratePdfReport.reportReport(report);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @RequestMapping(value = "/historyreport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> historyReport() throws IOException {
        List<History> history = (List<History>) historyService.findAllByOrderByUserIdAsc();
        List<User> users = (List<User>) userService.findAllByOrderByUserIdAsc();

        ByteArrayInputStream bis = GeneratePdfReport.historyReport(history, users);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=History.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @RequestMapping(value = "/hotspotreport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> hotspotReport() throws IOException {
        List<Hotspot> hotspots = (List<Hotspot>) hotspotService.findAllByOrderByIdAsc();

        ByteArrayInputStream bis = GeneratePdfReport.hotspotReport(hotspots);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Hotspot.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }
}
