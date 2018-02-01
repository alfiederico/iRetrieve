/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.controller;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Message;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class SettleController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @RequestMapping(value = {"/settle"}, method = RequestMethod.GET)
    public ModelAndView settle(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Report report = reportService.findByUserId(user.getUserId());

        ModelAndView modelAndView = new ModelAndView();

        if (report == null) {
            modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
            modelAndView.addObject("adminMessage", "Report item not found. Report item first.");
            modelAndView.setViewName("/admin/home");

        } else {
            modelAndView.setViewName("settle");
            model.addAttribute("report", report);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/mobile/settle", method = RequestMethod.GET)
    public @ResponseBody
    Report getSettle(@RequestParam(value = "userid", required = true) String userid) {
        Report report = reportService.findByUserId(Integer.parseInt(userid));
        if (report != null) {
            return report;
        } else {
            return null;
        }

    }

    @RequestMapping(value = "/mobile/isettlefeed", method = RequestMethod.GET)
    public @ResponseBody
    List<Report> settlefeedReport(@RequestParam(value = "isettle", required = true) String isettle) {

        try {

            List<Report> arrReport = reportService.findAllByOrderByUserIdAsc();

            List<Report> arrReport2 = new ArrayList<Report>();

            for (Report e : arrReport) {
                if (e.getIsettle() == Integer.parseInt(isettle)) {
                    arrReport2.add(e);
                }
            }

            if (arrReport2.size() < 1) {
                return null;

            } else {
                return arrReport2;
            }

        } catch (Exception ex) {
            return null;
        }

    }

    @RequestMapping(value = "/mobile/isettle", method = RequestMethod.GET)
    public @ResponseBody
    Message settleReport(@RequestParam(value = "isettle", required = true) String isettle, @RequestParam(value = "userid", required = true) String userid) {

        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Report report = reportService.findByUserId(Integer.parseInt(userid));
            Report reportB = reportService.findById(Integer.parseInt(isettle));

            if (report.getId() == reportB.getId()) {
                return new Message(4, "settleid and reportid supplied cannot be the same.");
            }

            report.setIsettle(Integer.parseInt(isettle));

            boolean bSettle = false;
            if (reportB != null) {
                reportB.setUsettle(report.getId());

                if (report.getIsettle() == reportB.getId() && report.getUsettle() == reportB.getId()) {
                    if (reportB.getIsettle() == report.getId() && reportB.getUsettle() == report.getId()) {
                        bSettle = true;
                        report.setStatus("Done");
                        reportB.setStatus("Done");

                        History a = new History();
                        a.setTypeId(report.getId());
                        a.setUserId(report.getUserId());
                        a.setType(report.getType());
                        a.setSubject(report.getSubject());
                        a.setDescription(report.getDescription());
                        a.setDate(report.getDate());
                        a.setLocation(report.getLocation());
                        a.setSettleId(report.getIsettle());
                        a.setPlace(report.getPlace());

                        historyService.saveHistory(a);

                        History b = new History();
                        b.setTypeId(reportB.getId());
                        b.setUserId(reportB.getUserId());
                        b.setType(reportB.getType());
                        b.setSubject(reportB.getSubject());
                        b.setDescription(reportB.getDescription());
                        b.setDate(reportB.getDate());
                        b.setLocation(reportB.getLocation());
                        b.setSettleId(reportB.getIsettle());
                        b.setPlace(reportB.getPlace());

                        historyService.saveHistory(b);

                        reportService.deleteReport(report);
                        reportService.deleteReport(reportB);

                    }
                }
                if (bSettle == false) {
                    reportService.saveReport(reportB);
                }

            }
            if (bSettle == false) {
                reportService.saveReport(report);
            }

            if (bSettle == true) {
                return new Message(1, "Report item already settle. Details go to History");
            } else {
                return new Message(2, "Please wait for other party to settle");
            }
        } catch (Exception ex) {
            return new Message(3, ex.getMessage());
        }

    }

    @RequestMapping(value = {"/settle"}, method = RequestMethod.POST)
    public ModelAndView updateSettle(ModelAndView modelAndView, @RequestParam("paramName") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Report report = reportService.findByUserId(user.getUserId());
        Report reportB = reportService.findById(Integer.parseInt(id));
        report.setIsettle(Integer.parseInt(id));

        boolean bSettle = false;
        if (reportB != null) {
            reportB.setUsettle(report.getId());

            if (report.getIsettle() == reportB.getId() && report.getUsettle() == reportB.getId()) {
                if (reportB.getIsettle() == report.getId() && reportB.getUsettle() == report.getId()) {
                    bSettle = true;
                    report.setStatus("Done");
                    reportB.setStatus("Done");

                    History a = new History();
                    a.setTypeId(report.getId());
                    a.setUserId(report.getUserId());
                    a.setType(report.getType());
                    a.setSubject(report.getSubject());
                    a.setDescription(report.getDescription());
                    a.setDate(report.getDate());
                    a.setLocation(report.getLocation());
                    a.setSettleId(report.getIsettle());
                    a.setPlace(report.getPlace());

                    historyService.saveHistory(a);

                    History b = new History();
                    b.setTypeId(reportB.getId());
                    b.setUserId(reportB.getUserId());
                    b.setType(reportB.getType());
                    b.setSubject(reportB.getSubject());
                    b.setDescription(reportB.getDescription());
                    b.setDate(reportB.getDate());
                    b.setLocation(reportB.getLocation());
                    b.setSettleId(reportB.getIsettle());
                    b.setPlace(reportB.getPlace());

                    historyService.saveHistory(b);

                    reportService.deleteReport(report);
                    reportService.deleteReport(reportB);

                }
            }
            if (bSettle == false) {
                reportService.saveReport(reportB);
            }

        }
        if (bSettle == false) {
            reportService.saveReport(report);
        }

        if (bSettle == true) {
            modelAndView.addObject("adminMessage", "Report item already settle. Details go to History");
        } else {
            modelAndView.addObject("adminMessage", "Please wait for other party to settle");
        }

        modelAndView.setViewName("/admin/home");
        return modelAndView;
    }
}
