/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.controller;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Report;
import com.iRetrieve.cloud.domain.User;
import com.iRetrieve.cloud.service.HistoryService;
import com.iRetrieve.cloud.service.ReportService;
import com.iRetrieve.cloud.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class HistoryController {

    @Autowired
    private UserService userService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ModelAndView settle(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<History> arrHistory = historyService.findAllByOrderByUserIdAsc();

        List<History> arrHistory2 = new ArrayList<>();

        if (arrHistory != null) {
            for (History e : arrHistory) {
                if (e.getUserId() == user.getUserId()) {
                    arrHistory2.add(e);
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView();

        if (arrHistory2.size() < 1) {
            modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
            modelAndView.addObject("adminMessage", "History item not found. settle item first.");
            modelAndView.setViewName("/admin/home");

        } else {
            modelAndView.setViewName("history");
            model.addAttribute("history", arrHistory2);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/mobile/history", method = RequestMethod.GET)
    public @ResponseBody
    List<History> getHistory(@RequestParam(value = "userid", required = true) String userid) {

        List<History> arrHistory = historyService.findAllByOrderByUserIdAsc();

        List<History> arrHistory2 = new ArrayList<>();

        if (arrHistory != null) {
            for (History e : arrHistory) {
                if (e.getUserId() == Integer.parseInt(userid)) {
                    arrHistory2.add(e);
                }
            }
        }

        if (arrHistory2.size() < 1) {
            return null;

        } else {
            return arrHistory2;
        }

    }

    @RequestMapping(value = "/mobile/historystats", method = RequestMethod.GET)
    public @ResponseBody
    List<History> getHistoryID(@RequestParam(value = "id", required = true) String id) {

        List<History> arrHistory = historyService.findAllByOrderByUserIdAsc();

        List<History> arrHistory2 = new ArrayList<>();

        List<Report> arrReport = reportService.findAllByOrderByUserIdAsc();

        if (arrHistory != null) {
            for (History e : arrHistory) {
                if (e.getId() >= Integer.parseInt(id)) {
                    arrHistory2.add(e);
                }
            }
        }

        if (arrReport != null) {
            for (Report e : arrReport) {
                if (e.getType().equals("LOST")) {
                    History h = new History();
                    h.setType("-LOST");
                    h.setDate(e.getDate());

                    arrHistory2.add(h);
                }
            }
        }

        if (arrHistory2.size() < 1) {
            return null;

        } else {
            return arrHistory2;
        }

    }
}
