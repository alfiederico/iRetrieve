/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.controller;

import com.iRetrieve.cloud.domain.Message;
import com.iRetrieve.cloud.domain.Report;
import com.iRetrieve.cloud.domain.User;
import com.iRetrieve.cloud.service.HistoryService;
import com.iRetrieve.cloud.service.HotspotService;
import com.iRetrieve.cloud.service.ReportService;
import com.iRetrieve.cloud.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Alfie
 */
@Controller
public class SettingController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HotspotService hotspotService;

    @RequestMapping(value = {"/setting"}, method = RequestMethod.GET)
    public ModelAndView settle(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("setting");
        model.addAttribute("user", user);

        return modelAndView;
    }

    @RequestMapping(value = "/mobile/setting", method = RequestMethod.GET)
    public @ResponseBody
    User getSetting(@RequestParam(value = "userid", required = true) String userid) {
        User user = userService.findUserByUserId(Integer.parseInt(userid));
        if (user != null) {
            return user;
        } else {
            return null;
        }

    }

    @RequestMapping(value = "/mobile/updateSetting", method = RequestMethod.POST)
    public @ResponseBody
    User updateUser(@RequestBody User user) {
        User userExists = userService.findUserByUserId(user.getUserId());
        if (userExists != null) {
            userExists.setName(user.getName());
            userExists.setLastName(user.getLastName());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            userExists.setPassword(encoder.encode(user.getPassword()));
            userExists.setPhone(user.getPhone());
            userExists.setRadius(user.getRadius());
            userService.saveUser(userExists);
            return userExists;
        } else {

            return null;
        }

    }

    @RequestMapping(value = "/mobile/updateBalance", method = RequestMethod.GET)
    public @ResponseBody
    Message updateBalance(@RequestParam(value = "userid", required = true) String userid, @RequestParam(value = "total", required = true) int total, @RequestParam(value = "balance", required = true) String balance) {
        User userExists = userService.findUserByUserId(Integer.parseInt(userid));
        if (userExists != null) {
            
            int totalbalance = 0;
            
            if (balance.equals("token")) {
                totalbalance = userExists.getToken();
                totalbalance += total;
                userExists.setToken(totalbalance);
            } else {
                totalbalance = userExists.getPoints();
                totalbalance += total;
                userExists.setPoints(totalbalance);
            }

            userService.saveUser(userExists);
            return  new Message(0, "Your new " + balance + " balance is " + totalbalance, 0);
        } else {

            return null;
        }

    }

    @RequestMapping(value = "/setting", method = RequestMethod.POST)
    public ModelAndView updateUser(@Valid User user, BindingResult bindingResult, Model model) {
        ModelAndView modelAndView = new ModelAndView();

        User userExists = userService.findUserByEmail(user.getEmail());

        userExists.setName(user.getName());
        userExists.setLastName(user.getLastName());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userExists.setPassword(encoder.encode(user.getPassword()));

        userExists.setPhone(user.getPhone());
        userExists.setRadius(user.getRadius());

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("setting");
        } else {
            userService.saveUser(userExists);

            modelAndView.addObject("adminMessage", "PERSONAL DETAILS UPDATED");
            model.addAttribute("users", userService.findAllByOrderByUserIdAsc());
            model.addAttribute("reports", reportService.findAllByOrderByUserIdAsc());
            model.addAttribute("histories", historyService.findAllByOrderByUserIdAsc());
            model.addAttribute("hotspots", hotspotService.findAllByOrderByIdAsc());

            modelAndView.setViewName("/admin/home");

        }
        return modelAndView;
    }
}
