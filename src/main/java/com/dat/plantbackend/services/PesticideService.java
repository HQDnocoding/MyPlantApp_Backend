package com.dat.plantbackend.services;

import com.dat.plantbackend.enities.Disease;
import com.dat.plantbackend.enities.Pesticide;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PesticideService {

    Page<Pesticide> getPesticides(int page, int size, String name, String description);

    void deleteAllPesticidesById(List<Integer> ids);

    Pesticide cretePesticide(Pesticide pesticide);

    Page<Pesticide> getPesticideByNameAndPage(String name, int page, int size);
    Long getCountPesticide();

}
