package com.dat.plantbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PredictionResult {
    private String predictedClass;
    private double confidence;
    private long processingTimeMs;
    private List<TreatmentDTO> treatment;
}
