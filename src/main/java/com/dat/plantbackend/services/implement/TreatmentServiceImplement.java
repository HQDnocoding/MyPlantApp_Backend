package com.dat.plantbackend.services.implement;

import com.dat.plantbackend.common.CommonUtils;
import com.dat.plantbackend.dto.TreatmentDTO;
import com.dat.plantbackend.enities.Treatment;
import com.dat.plantbackend.repositories.TreatmentRepository;
import com.dat.plantbackend.services.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class TreatmentServiceImplement implements TreatmentService {
    @Autowired
    private TreatmentRepository treatmentRepository;

    @Override
    public Treatment getTreatmentById(int id) {
        return treatmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<TreatmentDTO> getTreatmentByDiseaseNameAndPlant(String disease, String plant) {
        return treatmentRepository.getTreatmentByDiseaseNameAndPlant(disease, plant);
    }

    @Override
    public Treatment saveTreatment(Treatment treatment) {
        if (treatment.getId() != null || CommonUtils.isNullOrEmpty(treatment.getInstruction())
                || CommonUtils.isNullOrEmpty(treatment.getDiseaseDescription())
                || CommonUtils.isNullOrEmpty(treatment.getDoseperacre())
                || treatment.getDisease() == null || treatment.getPlant() == null || treatment.getPesticide() == null) {
            return null;
        }
        try {
            return treatmentRepository.save(treatment);

        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    @Override
    public Treatment updateTreatment(Treatment treatment) {
        if (treatment.getId() == null) {
            return null;
        }
        return treatmentRepository.save(treatment);
    }

    @Override
    public boolean deleteTreatment(Treatment treatment) {
        if (treatment.getId() == null) {
            return false;
        }
        this.treatmentRepository.delete(treatment);
        return true;
    }

    @Override
    public List<Treatment> getAllTreatment() {
        return treatmentRepository.findAll();
    }

    @Override
    public Page<Treatment> getTreatments(Map<String, String> filters, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

        String disease = filters.get("disease");
        String pesticide = filters.get("pesticide");
        String plant = filters.get("plant");
        String dose = filters.get("dose");

        boolean hasFilter = filters.entrySet().stream()
                .anyMatch(e -> e.getValue() != null && !e.getValue().isEmpty()
                        && !"page".equals(e.getKey()) && !"size".equals(e.getKey()));
        try {
            if (hasFilter) {
                return treatmentRepository.searchTreatments(disease, pesticide, plant, dose, pageable);
            } else {
                return treatmentRepository.findAll(pageable);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    @Override
    public void deleteAllTreatmentsById(List<Integer> ids) {
        ids.forEach(id -> this.treatmentRepository.deleteById(id));
    }
}
