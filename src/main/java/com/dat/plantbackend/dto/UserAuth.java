package com.dat.plantbackend.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuth {
    private String id;
    private String name;
    private String email;
    private Object picture;
}
