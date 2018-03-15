/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.service.impl;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Hotspot;
import com.iRetrieve.cloud.domain.Report;
import com.iRetrieve.cloud.repository.HotspotRepository;
import com.iRetrieve.cloud.repository.ReportRepository;
import com.iRetrieve.cloud.service.HotspotService;
import com.iRetrieve.cloud.service.ReportService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alfie
 */
@Service("hotspotService")
public class HotspotServiceImpl implements HotspotService {

    @Autowired
    private HotspotRepository hotspotRepository;

    @Override
    public Hotspot findById(int id) {
        return hotspotRepository.findById(id);
    }

    @Override
    public void saveHotspot(Hotspot hotspot) {

        hotspotRepository.save(hotspot);
    }

    @Override
    public void deleteHotspot(Hotspot hotspot) {
        hotspotRepository.delete(hotspot);
    }

    @Override
    public List<Hotspot> findAllByOrderByIdAsc() {
        return hotspotRepository.findAllByOrderByIdAsc();
    }

}
