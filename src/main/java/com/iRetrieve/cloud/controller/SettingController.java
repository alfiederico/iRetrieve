/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.controller;

import com.iRetrieve.cloud.domain.Report;
import com.iRetrieve.cloud.domain.User;
import com.iRetrieve.cloud.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Alfie
 */
@Controller
public class SettingController {

    @Autowired
    private UserService userService;

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
            userExists.setPassword(user.getPassword());
            userExists.setPhone(user.getPhone());
            userExists.setRadius(user.getRadius());
            userService.saveUser(userExists);
            return userExists;
        } else {

            return null;
        }

    }

    @RequestMapping(value = "/setting", method = RequestMethod.POST)
    public ModelAndView updateUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        User userExists = userService.findUserByEmail(user.getEmail());

        userExists.setName(user.getName());
        userExists.setLastName(user.getLastName());
        userExists.setPassword(user.getPassword());
        userExists.setPhone(user.getPhone());
        userExists.setRadius(user.getRadius());

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(userExists);
            modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
            modelAndView.addObject("adminMessage", "Personal details updated");
            modelAndView.setViewName("/admin/home");

        }
        return modelAndView;
    }
}
