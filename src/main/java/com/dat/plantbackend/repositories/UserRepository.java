package com.dat.plantbackend.repositories;

import com.dat.plantbackend.enities.User;
import com.dat.plantbackend.utils.SqlQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = SqlQuery.User.FIND_USER_BY_EMAIL_AND_TYPE_ACCOUNT, nativeQuery = true)
    Optional<User> findUsersByEmailAndTypeAccount(String email, String typeAccount);

    User getUsersById(UUID id);


}
