package com.dat.plantbackend.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {
    UUID id;
    String name;
    String email;

    public UserDTO(UUID id,String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
