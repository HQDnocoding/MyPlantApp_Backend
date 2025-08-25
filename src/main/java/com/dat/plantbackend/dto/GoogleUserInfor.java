package com.dat.plantbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserInfor {
    private String googleId;
    private String email;
    private String name;
    private String picture;
    private Boolean emailVerified;
}