package com.dat.plantbackend.services;


import com.dat.plantbackend.enities.Plant;
import org.springframework.data.domain.Page;

public interface PlantService {
    Page<Plant> getPlantsByName(String name, int page, int size);


}
