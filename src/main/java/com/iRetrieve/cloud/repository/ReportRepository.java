/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.repository;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Report;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alfie
 */
@Repository("reportRepository")
public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findById(int id);

    Report findByUserId(int user_id);
    
    List<Report> findAllByOrderByUserIdAsc();
    


}
