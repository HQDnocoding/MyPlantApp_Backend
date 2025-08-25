package com.dat.plantbackend.repositories;

import com.dat.plantbackend.enities.History;
import com.dat.plantbackend.utils.SqlQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Repository
@Transactional
public interface HistoryRepository extends JpaRepository<History, Integer> {

    @Query(value = SqlQuery.History.FIND_HISTORY_BY_USER_ID,
            countQuery = SqlQuery.History.COUNT_HISTORY_BY_USER_ID,
            nativeQuery = true)
    Page<History> getHistoryByUserId(@Param("userId") UUID userId, Pageable pageable);


    List<History> getHistoriesById(UUID id);


    @Modifying
    @Query(value = SqlQuery.History.DELETE_HISTORY_BY_ID, nativeQuery = true)
    void deleteHistoriesById(UUID id);


    @Query(value = SqlQuery.History.FIND_HISTORY_BY_ID, nativeQuery = true)
    History getHistoryById(UUID id);
}
