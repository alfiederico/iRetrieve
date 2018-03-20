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
public class HotspotController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HotspotService hotspotService;

    @RequestMapping(value = "/hotspot", method = RequestMethod.GET)
    public ModelAndView hotspot() {
        ModelAndView modelAndView = new ModelAndView();
        Hotspot hotspot = new Hotspot();
        modelAndView.addObject("hotspot", hotspot);
        modelAndView.setViewName("hotspot");
        return modelAndView;
    }
    
    @RequestMapping(value = "/hotspotget", method = RequestMethod.GET)
    public ModelAndView gethotspot(@RequestParam(value = "id", required = true) String id, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Hotspot hotspot = hotspotService.findById(Integer.parseInt(id));
        model.addAttribute("hotspot", hotspot);
        modelAndView.setViewName("hotspotupdate");
        return modelAndView;
    }

    @RequestMapping(value = "/hotspot", method = RequestMethod.POST)
    public ModelAndView createNewHotspot(@Valid Hotspot hotspot, BindingResult bindingResult, Model model) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("hotspot");
        } else {

            java.text.SimpleDateFormat sdf
                    = new java.text.SimpleDateFormat("dd-mm-yyyy HH:mm:ss");

            try {
                hotspot.setDate_created(sdf.format(new java.util.Date()));
                hotspot.setLast_updated(sdf.format(new java.util.Date()));
            } catch (Exception ex) {

            }

            hotspotService.saveHotspot(hotspot);
            modelAndView.addObject("adminMessage", "Hotspot has been created successfully");
            model.addAttribute("users", userService.findAllByOrderByUserIdAsc());
            model.addAttribute("reports", reportService.findAllByOrderByUserIdAsc());
            model.addAttribute("histories", historyService.findAllByOrderByUserIdAsc());
            model.addAttribute("hotspots", hotspotService.findAllByOrderByIdAsc());
            modelAndView.setViewName("/admin/home");

        }
        return modelAndView;
    }
    
        @RequestMapping(value = "/hotspotget", method = RequestMethod.POST)
    public ModelAndView updateHotspot(@Valid Hotspot hotspot, BindingResult bindingResult, Model model) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("hotspotupdate");
        } else {
            
            Hotspot hotspotupdate = hotspotService.findById(hotspot.getId());

            java.text.SimpleDateFormat sdf
                    = new java.text.SimpleDateFormat("dd-mm-yyyy HH:mm:ss");

            try {
                hotspotupdate.setDate_created(sdf.format(new java.util.Date()));
                hotspotupdate.setLast_updated(sdf.format(new java.util.Date()));
            } catch (Exception ex) {

            }
            
            hotspotupdate.setName(hotspot.getName());
            hotspotupdate.setContact(hotspot.getContact());
            hotspotupdate.setPlace(hotspot.getPlace());
            

            hotspotService.saveHotspot(hotspotupdate);
            modelAndView.addObject("adminMessage", "Hotspot has been created successfully");
            model.addAttribute("users", userService.findAllByOrderByUserIdAsc());
            model.addAttribute("reports", reportService.findAllByOrderByUserIdAsc());
            model.addAttribute("histories", historyService.findAllByOrderByUserIdAsc());
            model.addAttribute("hotspots", hotspotService.findAllByOrderByIdAsc());
            modelAndView.setViewName("/admin/home");

        }
        return modelAndView;
    }
}
