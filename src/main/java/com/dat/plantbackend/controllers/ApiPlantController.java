package com.dat.plantbackend.controllers;


import com.dat.plantbackend.enities.Plant;
import com.dat.plantbackend.services.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiPlantController {

    @Autowired
    private PlantService plantService;

    @GetMapping("/secure/plants")
    public Page<Plant> getPlants( @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(required = false) String keyword ){
        return plantService.getPlantsByName(keyword, page, size);
    }

}
