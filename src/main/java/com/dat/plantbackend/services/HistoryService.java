package com.dat.plantbackend.services;

import com.dat.plantbackend.dto.CreateHistoryDTO;
import com.dat.plantbackend.dto.HistoryDTO;
import com.dat.plantbackend.enities.History;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface HistoryService {
    History saveHistory(CreateHistoryDTO history, UUID uuid);
    Page<HistoryDTO> getHistoryByUUID(UUID uuid,int page, int size) ;
    void deleteHistoryById(UUID hId, UUID uuid);
}
