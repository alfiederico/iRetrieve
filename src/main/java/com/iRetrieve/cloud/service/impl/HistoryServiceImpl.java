/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.service.impl;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Report;
import com.iRetrieve.cloud.repository.HistoryRepository;
import com.iRetrieve.cloud.repository.ReportRepository;
import com.iRetrieve.cloud.service.HistoryService;
import com.iRetrieve.cloud.service.ReportService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alfie
 */
@Service("historyService")
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Override
    public History findById(int id) {
        return historyRepository.findById(id);
    }

    @Override
    public void saveHistory(History history) {

        historyRepository.save(history);
    }

    @Override
    public History findByUserId(int user_id) {
       return historyRepository.findByUserId(user_id);
    }

    @Override
    public List<History> findAllByOrderByUserIdAsc() {
             return historyRepository.findAllByOrderByUserIdAsc();
    }
}
