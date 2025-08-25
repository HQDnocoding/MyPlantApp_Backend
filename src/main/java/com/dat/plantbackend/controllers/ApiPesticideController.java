package com.dat.plantbackend.controllers;


import com.dat.plantbackend.enities.Disease;
import com.dat.plantbackend.enities.Pesticide;
import com.dat.plantbackend.services.implement.PesticideServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiPesticideController {

    @Autowired
    private PesticideServiceImplement pesticideService;


    @DeleteMapping("/secure/pesticides")
    public ResponseEntity<Void> deletePesticides(@RequestBody List<Integer> ids) {
        pesticideService.deleteAllPesticidesById(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/secure/pesticides")
    public Page<Pesticide> getDiseases(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword) {

        return this.pesticideService.getPesticideByNameAndPage(keyword, page, size);
    }

}
