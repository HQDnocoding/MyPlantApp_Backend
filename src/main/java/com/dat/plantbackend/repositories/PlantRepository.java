package com.dat.plantbackend.repositories;

import com.dat.plantbackend.enities.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface PlantRepository extends JpaRepository<Plant, Integer> {

    Page<Plant> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
