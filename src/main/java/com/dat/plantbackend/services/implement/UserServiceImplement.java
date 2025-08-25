package com.dat.plantbackend.services.implement;

import com.dat.plantbackend.common.Types;
import com.dat.plantbackend.dto.FacebookUserInfo;
import com.dat.plantbackend.dto.GoogleUserInfor;
import com.dat.plantbackend.enities.User;
import com.dat.plantbackend.repositories.UserRepository;
import com.dat.plantbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplement implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findOrCreateUser(GoogleUserInfor googleUser) {
        return this.findOrCreateUser(googleUser.getEmail(), googleUser.getName(),Types.TypeAccount.GOOGLE.toString());
    }

    @Override
    public User getUserById(UUID id) {
        return this.userRepository.getUsersById(id);
    }

    @Override
    public User findOrCreateUser(FacebookUserInfo fbUser) {
        return this.findOrCreateUser(fbUser.getEmail(), fbUser.getName(),Types.TypeAccount.FACEBOOK.toString());
    }

    private User findOrCreateUser(String email, String name,String typeAccount) {
        Optional<User> existingUser = userRepository.findUsersByEmailAndTypeAccount(email,typeAccount);
        if (existingUser.isPresent()) {
            System.out.println("User already exists: " + existingUser.get().getUsername());
            User user = existingUser.get();
            user.setName(name);
            return userRepository.save(user);
        } else {
            System.out.println("User does not exist. Creating new user: " + email);
            User newUser = User.builder()
                    .username(email).name(name)
                    .typeAccount(typeAccount)
                    .createAt(Instant.now())
                    .role(Types.Roles.ROLE_NORMAL_USER.toString())
                    .build();
            User u= (User)userRepository.save(newUser);
            System.out.println("User created: " + u.getUsername());
            return u;
        }
    }

    @Override
    public Long getCountUser() {
        return this.userRepository.count();
    }
}
