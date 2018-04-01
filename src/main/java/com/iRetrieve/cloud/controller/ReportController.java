/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.controller;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Hotspot;
import com.iRetrieve.cloud.domain.Report;
import com.iRetrieve.cloud.domain.User;
import com.iRetrieve.cloud.service.HistoryService;
import com.iRetrieve.cloud.service.HotspotService;
import com.iRetrieve.cloud.service.ReportService;
import com.iRetrieve.cloud.service.UserService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

    @Autowired
    private HotspotService hotspotService;

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

        Report reportOld = null;

        List<Report> reports = reportService.findAllByOrderByUserIdAsc();

        for (Report e : reports) {
            if (e.getUserId() == report.getUserId()) {
                reportOld = e;
            }
        }
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

                String[] current = report.getLocation().split(",");
                boolean bFlag = true;

                if (reports.size() > 0) {
                    for (Report f : reports) {
                        String[] currentF = f.getLocation().split(",");
                        if (distance(Double.parseDouble(current[0]), Double.parseDouble(current[1]), Double.parseDouble(currentF[0]), Double.parseDouble(currentF[1]), 'K') <= 1) {
                            report.setLocation(f.getLocation());
                            bFlag = false;
                            break;
                        }
                    }
                }

                if (bFlag) {
                    List<Hotspot> hotspots = hotspotService.findAllByOrderByIdAsc();
                    if (hotspots.size() > 0) {
                        for (Hotspot g : hotspots) {
                            String[] currentG = g.getLocation().split(",");
                            if (distance(Double.parseDouble(current[0]), Double.parseDouble(current[1]), Double.parseDouble(currentG[0]), Double.parseDouble(currentG[1]), 'K') <= 1) {
                                report.setLocation(g.getLocation());
                                break;
                            }
                        }
                    }
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

    public Double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else {
            dist = dist * 0.8684;
        }

        return dist;
    }

    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public double rad2deg(double rad) {
        return rad / Math.PI * 180.0;
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
    public ModelAndView createNewReport(@Valid Report report, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        ModelAndView modelAndView = null;

        if (bindingResult.hasErrors()) {
            modelAndView = new ModelAndView();
            modelAndView.setViewName("report");
        } else {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByEmail(auth.getName());
            report.setUserId(user.getUserId());

            Report reportOld = null;

            List<Report> reports = reportService.findAllByOrderByUserIdAsc();

            for (Report e : reports) {
                if (e.getUserId() == report.getUserId()) {
                    reportOld = e;
                }
            }

            if (reportOld == null) {
                java.text.SimpleDateFormat sdf
                        = new java.text.SimpleDateFormat("dd-mm-yyyy HH:mm:ss");

                try {
                    Date date = (sdf.parse(report.getDate()));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    
                    String sDate = day + "-" + month + "-" + year;

                    //report.setDate(sdf.format(sdf.parse(report.getDate())));
                    report.setDate(sDate);
                    report.setDate_created(sdf.format(new java.util.Date()));
                    report.setLast_updated(sdf.format(new java.util.Date()));
                } catch (Exception ex) {
                    String[] dates = report.getDate().split("-");
                    String date = Integer.parseInt(dates[2]) + "-" + Integer.parseInt(dates[1]) + "-" + dates[0];
                    report.setDate(date);
                    report.setDate_created(date);
                    report.setLast_updated(date);
                }

                String[] current = report.getLocation().split(",");
                boolean bFlag = true;

                if (reports.size() > 0) {
                    for (Report f : reports) {
                        String[] currentF = f.getLocation().split(",");
                        if (distance(Double.parseDouble(current[0]), Double.parseDouble(current[1]), Double.parseDouble(currentF[0]), Double.parseDouble(currentF[1]), 'K') <= 1) {
                            report.setLocation(f.getLocation());
                            bFlag = false;
                            break;
                        }
                    }
                }

                if (bFlag) {
                    List<Hotspot> hotspots = hotspotService.findAllByOrderByIdAsc();
                    if (hotspots.size() > 0) {
                        for (Hotspot g : hotspots) {
                            String[] currentG = g.getLocation().split(",");
                            if (distance(Double.parseDouble(current[0]), Double.parseDouble(current[1]), Double.parseDouble(currentG[0]), Double.parseDouble(currentG[1]), 'K') <= 1) {
                                report.setLocation(g.getLocation());
                                break;
                            }
                        }
                    }
                }

                report.setIsettle(0);
                report.setUsettle(0);
                reportService.saveReport(report);
                modelAndView = new ModelAndView(new RedirectView("/admin/home"));
                ra.addFlashAttribute("adminMessage", "REPORT HAS BEEN CREATED SUCCESSFULLY");

            } else {
                modelAndView = new ModelAndView();
                modelAndView.addObject("successMessage", "Please settle previous report before add new one");
                modelAndView.addObject("report", new Report());
                modelAndView.setViewName("report");
            }

        }
        return modelAndView;
    }
}
