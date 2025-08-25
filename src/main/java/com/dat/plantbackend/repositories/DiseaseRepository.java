package com.dat.plantbackend.repositories;

import com.dat.plantbackend.enities.Disease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface DiseaseRepository extends JpaRepository<Disease,Integer> {

    Page<Disease> findDiseaseByNameContainingIgnoreCase(String name, Pageable pageable);
}
