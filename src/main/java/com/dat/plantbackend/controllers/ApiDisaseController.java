package com.dat.plantbackend.controllers;


import com.dat.plantbackend.enities.Disease;
import com.dat.plantbackend.services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiDisaseController {
    @Autowired
    private DiseaseService diseaseService;


    @GetMapping("/secure/diseases")
    public Page<Disease> getDiseases(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword) {

        return diseaseService.getDiseaseByNameAndPage(keyword, page, size);
    }
}
