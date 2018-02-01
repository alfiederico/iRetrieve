/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.service.impl;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Report;
import com.iRetrieve.cloud.repository.ReportRepository;
import com.iRetrieve.cloud.service.ReportService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alfie
 */
@Service("reportService")
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public Report findById(int id) {
        return reportRepository.findById(id);
    }

    @Override
    public void saveReport(Report report) {

        reportRepository.save(report);
    }

    @Override
    public Report findByUserId(int user_id) {
       return reportRepository.findByUserId(user_id);
    }

    @Override
    public void deleteReport(Report report) {
         reportRepository.delete(report);
    }
    
        @Override
    public List<Report> findAllByOrderByUserIdAsc() {
             return reportRepository.findAllByOrderByUserIdAsc();
    }


}
