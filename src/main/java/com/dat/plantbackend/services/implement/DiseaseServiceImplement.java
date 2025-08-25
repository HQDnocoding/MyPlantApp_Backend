package com.dat.plantbackend.services.implement;


import com.dat.plantbackend.common.CommonUtils;
import com.dat.plantbackend.enities.Disease;
import com.dat.plantbackend.repositories.DiseaseRepository;
import com.dat.plantbackend.services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DiseaseServiceImplement implements DiseaseService {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Override
    public Page<Disease> getDiseaseByNameAndPage(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        if(CommonUtils.isNullOrEmpty(name)){
            return this.diseaseRepository.findAll(pageable);
        }else{
            return this.diseaseRepository.findDiseaseByNameContainingIgnoreCase(name, pageable);
        }
    }
}
