/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.service;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Report;
import com.iRetrieve.cloud.domain.User;
import java.util.List;

/**
 *
 * @author Alfie
 */
public interface HistoryService {

    public History findById(int id);

    History findByUserId(int user_id);
    
    List<History> findAllByOrderByUserIdAsc();
    
    

    public void saveHistory(History history);
}
