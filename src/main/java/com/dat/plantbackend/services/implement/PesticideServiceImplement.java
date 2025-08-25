package com.dat.plantbackend.services.implement;


import com.dat.plantbackend.common.CommonUtils;
import com.dat.plantbackend.enities.Pesticide;
import com.dat.plantbackend.repositories.PesticideRepository;
import com.dat.plantbackend.services.PesticideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PesticideServiceImplement implements PesticideService {

    @Autowired
    private PesticideRepository pesticideRepository;

    @Override
    public Page<Pesticide> getPesticides(int page, int size, String name, String description) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

        if ((name == null || name.isBlank()) && (description == null || description.isBlank())) {
            return pesticideRepository.findAll(pageable);
        } else {
            return this.pesticideRepository.findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(
                    name == null ? "" : name.trim(),
                    description == null ? "" : description.trim(),
                    pageable
            );
        }
    }

    @Override
    public void deleteAllPesticidesById(List<Integer> ids) {
        ids.forEach(id -> this.pesticideRepository.deleteById(id));
    }

    @Override
    public Pesticide cretePesticide(Pesticide pesticide) {

        return this.pesticideRepository.save(pesticide);
    }

    @Override
    public Page<Pesticide> getPesticideByNameAndPage(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

        if (CommonUtils.isNullOrEmpty(name)) {
            return this.pesticideRepository.findAll(pageable);
        }else {
            return this.pesticideRepository.findByNameContainingIgnoreCase(name, pageable);
        }
    }

    @Override
    public Long getCountPesticide() {
        return this.pesticideRepository.count();
    }
}
