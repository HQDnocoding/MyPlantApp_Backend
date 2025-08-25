package com.dat.plantbackend.services.implement;

import com.dat.plantbackend.common.Types;
import com.dat.plantbackend.enities.User;
import com.dat.plantbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUsersByEmailAndTypeAccount(username, Types.TypeAccount.PASSWORD.toString());
        if (user.isPresent()) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(user.get().getRole().trim()));

            return new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(), user.get().getPassword(), authorities
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }


}
