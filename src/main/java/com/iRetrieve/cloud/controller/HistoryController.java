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
import com.iRetrieve.cloud.service.HotspotService;
import com.iRetrieve.cloud.service.ReportService;
import com.iRetrieve.cloud.service.UserService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class HistoryController {

    @Autowired
    private UserService userService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private ReportService reportService;
            
    @Autowired
    private HotspotService hotspotService;

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ModelAndView settle(Model model, RedirectAttributes ra) {
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

        ModelAndView  modelAndView = new ModelAndView();

        if (arrHistory2.size() < 1) {
            model.addAttribute("users", userService.findAllByOrderByUserIdAsc());
            model.addAttribute("reports", reportService.findAllByOrderByUserIdAsc());
            model.addAttribute("histories", historyService.findAllByOrderByUserIdAsc());
            model.addAttribute("hotspots", hotspotService.findAllByOrderByIdAsc());

            modelAndView.addObject("adminMessage", "HISTORY ITEM NOT FOUND. SETTLE ITEM FIRST.");
            
            modelAndView.setViewName("/admin/home");
                     
        } else{ 
            modelAndView = new ModelAndView();
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
    
    @RequestMapping(value = "/historyget", method = RequestMethod.GET)
    public ModelAndView gethistory(@RequestParam(value = "id", required = true) String id, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        History history = historyService.findById(Integer.parseInt(id));
        User user = userService.findUserByUserId(history.getUserId());
        model.addAttribute("history", history);
        model.addAttribute("user", user);
        modelAndView.setViewName("historyinfo");
        return modelAndView;
    }
}
