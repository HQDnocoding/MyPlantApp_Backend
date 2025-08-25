package com.dat.plantbackend.services;

import com.dat.plantbackend.enities.Disease;
import org.springframework.data.domain.Page;

public interface DiseaseService {

    Page<Disease> getDiseaseByNameAndPage(String name, int page, int size);

}
