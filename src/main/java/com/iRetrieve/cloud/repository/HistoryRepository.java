/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.repository;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Report;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alfie
 */
@Repository("historyRepository")
public interface HistoryRepository  extends JpaRepository<History, Long>{
     History findById(int id);
     History findByUserId(int user_id);
     List<History> findAllByOrderByUserIdAsc();
     
}
