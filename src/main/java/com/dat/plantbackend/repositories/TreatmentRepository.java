package com.dat.plantbackend.repositories;

import com.dat.plantbackend.dto.TreatmentDTO;
import com.dat.plantbackend.enities.Plant;
import com.dat.plantbackend.enities.Treatment;
import com.dat.plantbackend.utils.SqlQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {


    @Query(value = SqlQuery.Treatment.FIND_TREATMENT_BY_DISEASE_NAME_AND_PLANT, nativeQuery = true)
    List<TreatmentDTO> getTreatmentByDiseaseNameAndPlant(@Param("disease") String disease,@Param("plant") String plant);


    @Query(value = SqlQuery.Treatment.GET_TREATMENTS, nativeQuery = true)
    Page<Treatment> searchTreatments(  @Param("diseaseName") String diseaseName,
                                       @Param("pesticideName") String pesticideName,
                                       @Param("plantName") String plantName,
                                       @Param("dose") String dose,
                                     Pageable pageable);
}
