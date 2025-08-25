package com.dat.plantbackend.services;

import com.dat.plantbackend.dto.FacebookUserInfo;
import com.dat.plantbackend.dto.GoogleUserInfor;
import com.dat.plantbackend.enities.User;

import java.util.UUID;

public interface UserService {
    User findOrCreateUser(GoogleUserInfor googleUser);
    User findOrCreateUser(FacebookUserInfo googleUser);
    User getUserById(UUID id);
    Long getCountUser();
}
