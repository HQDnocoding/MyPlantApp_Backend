package com.dat.plantbackend.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreatmentDTO {
    private Integer id;
    private String dosePerAcre;
    private String instruction;
    private String diseaseName;
    private String pesticideName;
    private String pesticideDes;
    private String diseaseDescription;
}

