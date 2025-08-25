package com.dat.plantbackend.services.implement;


import com.dat.plantbackend.common.CommonUtils;
import com.dat.plantbackend.enities.Plant;
import com.dat.plantbackend.repositories.PlantRepository;
import com.dat.plantbackend.services.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PlantServiceImplement implements PlantService {

    @Autowired
    private PlantRepository plantRepository;
    @Override
    public Page<Plant> getPlantsByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

        if(CommonUtils.isNullOrEmpty(name)){
            return this.plantRepository.findAll(pageable);
        }else{
            return this.plantRepository.findByNameContainingIgnoreCase(name, pageable);
        }
    }
}
