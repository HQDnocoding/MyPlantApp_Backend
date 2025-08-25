package com.dat.plantbackend.dto;

import com.dat.plantbackend.enities.Treatment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
public class HistoryDTO {
    private TreatmentDTO treatment;
    private String predictedImage;
    private Instant createAt;
    private UUID id;
}
