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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private HistoryService historyService;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        Report report = new Report();
        modelAndView.addObject("report", report);
        modelAndView.setViewName("report");
        return modelAndView;
    }

    @RequestMapping(value = "/mobile/report", method = RequestMethod.POST)
    public @ResponseBody
    Report addNewReport(@RequestBody Report report) {

        Report reportOld = reportService.findByUserId(report.getUserId());
        try {
            if (reportOld == null) {
                java.text.SimpleDateFormat sdf
                        = new java.text.SimpleDateFormat("dd-mm-yyyy HH:mm:ss");

                try {
                    report.setDate(sdf.format(sdf.parse(report.getDate())));
                    report.setDate_created(sdf.format(new java.util.Date()));
                    report.setLast_updated(sdf.format(new java.util.Date()));
                } catch (Exception ex) {

                }

                report.setIsettle(0);
                report.setUsettle(0);
                reportService.saveReport(report);
                return report;

            } else {
                return null;
            }
        } catch (Exception ex) {
            String exx = ex.getMessage();
            System.out.println(ex.getMessage());
            return null;
        }

    }

    @RequestMapping(value = "/mobile/reports", method = RequestMethod.GET)
    public @ResponseBody
    List<Report> getHistory() {

        List<Report> arrReport = reportService.findAllByOrderByUserIdAsc();

        if (arrReport.size() < 1) {
            return null;

        } else {
            return arrReport;
        }

    }

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public ModelAndView createNewReport(@Valid Report report, BindingResult bindingResult, Model model) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("report");
        } else {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByEmail(auth.getName());
            report.setUserId(user.getUserId());

            Report reportOld = reportService.findByUserId(user.getUserId());

            if (reportOld == null) {
                java.text.SimpleDateFormat sdf
                        = new java.text.SimpleDateFormat("dd-mm-yyyy HH:mm:ss");

                try {
                    report.setDate(sdf.format(sdf.parse(report.getDate())));
                    report.setDate_created(sdf.format(new java.util.Date()));
                    report.setLast_updated(sdf.format(new java.util.Date()));
                } catch (Exception ex) {

                }

                report.setIsettle(0);
                report.setUsettle(0);
                reportService.saveReport(report);
                modelAndView.addObject("adminMessage", "Report has been created successfully");
                model.addAttribute("users", userService.findAllByOrderByUserIdAsc());
                model.addAttribute("reports", reportService.findAllByOrderByUserIdAsc());
                model.addAttribute("histories", historyService.findAllByOrderByUserIdAsc());
                modelAndView.setViewName("/admin/home");
            } else {
                modelAndView.addObject("successMessage", "Please settle previous report before add new one");
                modelAndView.addObject("report", new Report());
                modelAndView.setViewName("report");
            }

        }
        return modelAndView;
    }
}
