package com.dat.plantbackend.controllers;


import com.dat.plantbackend.services.TreatmentService;
import com.dat.plantbackend.services.implement.PesticideServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiTreatmentController {

    @Autowired
    private TreatmentService treatmentService;

    @DeleteMapping("/secure/treatments")
    public ResponseEntity<Void> deletePesticides(@RequestBody List<Integer> ids) {
        System.out.println("ids: " + ids);
        treatmentService.deleteAllTreatmentsById(ids);
        return ResponseEntity.noContent().build();
    }
}
