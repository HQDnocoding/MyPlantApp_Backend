package com.dat.plantbackend.repositories;

import com.dat.plantbackend.enities.Pesticide;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface PesticideRepository extends JpaRepository<Pesticide, Integer> {

    Page<Pesticide> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase
            (String name, String description, Pageable pageable);


    Page<Pesticide> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
