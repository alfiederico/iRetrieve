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
import com.iRetrieve.cloud.service.HotspotService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

    @Autowired
    private HotspotService hotspotService;

    @RequestMapping(value = {"/settle"}, method = RequestMethod.GET)
    public ModelAndView settle(Model model, RedirectAttributes ra) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Report report = reportService.findByUserId(user.getUserId());

        ModelAndView modelAndView = null;

        if (report == null) {
            modelAndView = new ModelAndView(new RedirectView("/admin/home"));
            ra.addFlashAttribute("adminMessage", "REPORT ITEM NOT FOUND. PLEASE REPORT ITEM FIRST.");
   

        } else {
            modelAndView = new ModelAndView();
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
                return new Message(4, "settleid and reportid supplied cannot be the same.", 0);
            }
            if (Integer.parseInt(isettle) < 1) {
                return new Message(5, "Lost/Found ID cannot be 0", 0);
            }

            report.setIsettle(Integer.parseInt(isettle));

            boolean bSettle = false;
            if (reportB != null) {
                reportB.setUsettle(report.getId());

                if (report.getIsettle() == reportB.getId() && report.getUsettle() == reportB.getId()) {
                    if (reportB.getIsettle() == report.getId() && reportB.getUsettle() == report.getId()) {
                        if (!report.getType().toUpperCase().equals(reportB.getType().toUpperCase())) {
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
                }
                if (bSettle == false) {
                    reportService.saveReport(reportB);
                }

            }
            if (bSettle == false) {
                reportService.saveReport(report);
            }

            if (bSettle == true) {
                return new Message(1, "Report item already settle. Details go to History", 0);
            } else {
                return new Message(2, "Please wait for other party to settle", 0);
            }
        } catch (Exception ex) {
            return new Message(3, ex.getMessage(), 0);
        }

    }

    @RequestMapping(value = {"/settle"}, method = RequestMethod.POST)
    public ModelAndView updateSettle(@RequestParam("paramName") String id, Model model, RedirectAttributes ra) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Report report = reportService.findByUserId(user.getUserId());
        Report reportB = reportService.findById(Integer.parseInt(id));
        report.setIsettle(Integer.parseInt(id));
        
        ModelAndView modelAndView = new ModelAndView(new RedirectView("/admin/home"));

        boolean bSettle = false;
        if (reportB != null) {
            reportB.setUsettle(report.getId());

            if (report.getIsettle() == reportB.getId() && report.getUsettle() == reportB.getId()) {
                if (reportB.getIsettle() == report.getId() && reportB.getUsettle() == report.getId()) {

                    if (!report.getType().toUpperCase().equals(reportB.getType().toUpperCase())) {
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
            }
            if (bSettle == false) {
                reportService.saveReport(reportB);
            }

        }
        if (bSettle == false) {
            reportService.saveReport(report);
        }

        if (bSettle == true) {
            ra.addFlashAttribute("adminMessage", "REPORT ITEM ALREADY SETTLE. DETAILS GO TO HISTORY");
        } else {
            ra.addFlashAttribute("adminMessage", "PLEASE WAIT FOR OTHER PARTY TO SETTLE");
        }


        return modelAndView;
    }
}
