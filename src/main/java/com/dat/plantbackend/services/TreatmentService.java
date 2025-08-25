package com.dat.plantbackend.services;

import com.dat.plantbackend.dto.TreatmentDTO;
import com.dat.plantbackend.enities.Treatment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface TreatmentService {
    Treatment getTreatmentById(int id);
    List<TreatmentDTO> getTreatmentByDiseaseNameAndPlant(String name, String plant);

    Treatment saveTreatment(Treatment treatment);
    Treatment updateTreatment(Treatment treatment);
    boolean deleteTreatment(Treatment treatment);
    List<Treatment> getAllTreatment();
    Page<Treatment> getTreatments(Map<String, String> filters, int page, int size);
    void deleteAllTreatmentsById(List<Integer> ids);
}
