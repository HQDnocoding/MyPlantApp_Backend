package com.dat.plantbackend.services.implement;

import com.cloudinary.Cloudinary;
import com.dat.plantbackend.common.CommonUtils;
import com.dat.plantbackend.dto.CreateHistoryDTO;
import com.dat.plantbackend.dto.HistoryDTO;
import com.dat.plantbackend.dto.TreatmentDTO;
import com.dat.plantbackend.enities.History;
import com.dat.plantbackend.enities.Treatment;
import com.dat.plantbackend.enities.User;
import com.dat.plantbackend.repositories.HistoryRepository;
import com.dat.plantbackend.repositories.TreatmentRepository;
import com.dat.plantbackend.services.HistoryService;
import com.dat.plantbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class HistoryServiceImplement implements HistoryService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private UserService userService;

    @Override
    public History saveHistory(CreateHistoryDTO history, UUID uuid) throws IllegalArgumentException {
        try {
            String imageUrl = CommonUtils.uploadFileToCloud(cloudinary, history.getPredictedImage());
            Treatment treatment = this.treatmentRepository.findById(history.getTreatmentId()).orElse(null);
            User u = this.userService.getUserById(uuid);
            System.out.println("User " + u.getId());
            return historyRepository.save(History.builder().predictedImage(imageUrl)
                    .treatment(treatment)
                    .user(u).build());

        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    @Override
    public Page<HistoryDTO> getHistoryByUUID(UUID uuid, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("create_at").descending());

        Page<History> historyPage = this.historyRepository.getHistoryByUserId(uuid, pageable);
        return historyPage.map(
                h -> HistoryDTO.builder().id(h.getId())
                        .treatment(TreatmentDTO.builder()
                                .diseaseName(h.getTreatment().getDisease().getName())
                                .diseaseDescription(h.getTreatment().getDiseaseDescription())
                                .instruction(h.getTreatment().getInstruction())
                                .dosePerAcre(h.getTreatment().getDoseperacre())
                                .pesticideName(h.getTreatment().getPesticide().getName())
                                .pesticideDes(h.getTreatment().getPesticide().getDescription()).build())
                        .predictedImage(h.getPredictedImage())
                        .createAt(h.getCreateAt()).build());

    }

    @Override
    public void deleteHistoryById(UUID hId, UUID uuid) {
        try {
            System.out.println("History id: " + hId);
            History history = this.historyRepository.getHistoryById(hId);
            System.out.println("History: " + history.getUser().getId());
            System.out.println("UUID: " + uuid);
            if (history.getUser().getId().compareTo(uuid) != 0) {
                throw new IllegalArgumentException("You don't have permission to delete this history");
            }
            this.historyRepository.deleteHistoriesById(hId);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
