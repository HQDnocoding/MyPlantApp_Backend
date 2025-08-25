package com.dat.plantbackend.controllers;


import com.dat.plantbackend.common.CommonUtils;
import com.dat.plantbackend.common.Types;
import com.dat.plantbackend.dto.*;
import com.dat.plantbackend.enities.User;
import com.dat.plantbackend.jwt.JwtTokenProvider;
import com.dat.plantbackend.services.FaceBookAuthService;
import com.dat.plantbackend.services.GoogleAuthService;
import com.dat.plantbackend.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    private final GoogleAuthService tokenService;

    private final UserService userService;

    private final FaceBookAuthService facebookAuthService;

    public AuthController(JwtTokenProvider jwtTokenProvider, GoogleAuthService tokenService, UserService userService, FaceBookAuthService facebookAuthService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenService = tokenService;
        this.userService = userService;
        this.facebookAuthService = facebookAuthService;
    }

    @PostMapping("/auth/google")
    public ResponseEntity<?> googleAuth(@RequestBody GoogleTokenRequest token) {
        try {
            System.out.println("Token: " + token);
            // Verify Google token...
            GoogleUserInfor googleUser = tokenService.verifyToken(token.getToken());
            System.out.println("Google user: " + googleUser);
            User user = userService.findOrCreateUser(googleUser);
            System.out.println("User: " + user.getId());
            List<String> roles = CommonUtils.isNullOrEmpty(user.getRole()) ? List.of(Types.Roles.ROLE_NORMAL_USER.toString()) : List.of(user.getRole());
            // Tạo tokens
            System.out.println(roles.getFirst());
            JwtTokenProvider.TokenPair tokenPair = jwtTokenProvider.generateTokenPair(user.getId(), roles);

            return ResponseEntity.ok(AuthResponse.builder()
                    .success(true)
                    .accessToken(tokenPair.getAccessToken())
                    .refreshToken(tokenPair.getRefreshToken())
                    .user(new UserDTO(user.getId(), user.getName(), user.getUsername()))
                    .expiresIn(tokenPair.getAccessTokenExpiresIn())
                    .refreshTokenExpiresIn(tokenPair.getRefreshTokenExpiresIn())
                    .message("Login successful.")
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .success(false)
                            .message(String.format("Authentication failed. Reason: " + e.getMessage()))
                            .build());
        }
    }

    @Getter
    @Setter
    public static class RequestFB {
        private String accessToken;
    }

    @PostMapping("/auth/facebook")
    public ResponseEntity<?> facebookAuth(@RequestBody RequestFB accessToken) {
        try {
            System.out.println("Facebook token: " + accessToken.accessToken);
            Boolean isTrustable = facebookAuthService.verifyToken(accessToken.accessToken);
            if (!isTrustable) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthResponse.builder()
                                .success(false)
                                .message("Facebook authentication failed")
                                .build());
            }

            FacebookUserInfo fbUser = this.facebookAuthService.getUserProfile(accessToken.accessToken);

            User user = userService.findOrCreateUser(fbUser);
            System.out.println("User: " + user.getId());

            List<String> roles = CommonUtils.isNullOrEmpty(user.getRole())
                    ? List.of(Types.Roles.ROLE_NORMAL_USER.toString())
                    : List.of(user.getRole());

            JwtTokenProvider.TokenPair tokenPair = jwtTokenProvider.generateTokenPair(user.getId(), roles);

            return ResponseEntity.ok(AuthResponse.builder()
                    .success(true)
                    .accessToken(tokenPair.getAccessToken())
                    .refreshToken(tokenPair.getRefreshToken())
                    .user(new UserDTO(user.getId(), user.getName(), user.getUsername()))
                    .expiresIn(tokenPair.getAccessTokenExpiresIn())
                    .refreshTokenExpiresIn(tokenPair.getRefreshTokenExpiresIn())
                    .message("Facebook login successful.")
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .success(false)
                            .message("Facebook authentication failed. Reason: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(Principal principal, @RequestBody Map<String, String> refreshToken) {
        try {
            String refreshTokenString = refreshToken.get("refreshToken");
            if(this.jwtTokenProvider.isTokenExpired(refreshTokenString)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthResponse.builder()
                                .success(false)
                                .message("Refresh token is expired.")
                                .build());
            }
            if(!Objects.equals(this.jwtTokenProvider.getTokenType(refreshTokenString), "refresh")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.builder()
                        .success(false).message("Refresh token is invalid.").build());
            }
            UUID uuid = jwtTokenProvider.getUuidFromToken(refreshTokenString);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String newAccessToken = jwtTokenProvider.generateAccessToken(uuid, roles);
            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .success(true)
                            .accessToken(newAccessToken)
                            .expiresIn(jwtTokenProvider.getTokenRemainingTime(newAccessToken)).build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .success(false)
                            .message(String.format("Authentication failed. Reason: " + e.getMessage()))
                            .build());
        }
    }

    @PostMapping("/secure/auth/refresh-all")
    public ResponseEntity<?> refreshAllToken(Principal principal, @RequestBody Map<String, String> refreshToken) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            JwtTokenProvider.TokenPair newTokenPair = jwtTokenProvider.refreshTokenPair(refreshToken.get("refreshToken"), roles);

            return ResponseEntity.ok(AuthResponse.builder()
                    .refreshTokenExpiresIn(newTokenPair.getRefreshTokenExpiresIn())
                    .accessToken(newTokenPair.getAccessToken())
                    .expiresIn(newTokenPair.getAccessTokenExpiresIn())
                    .refreshTokenExpiresIn(newTokenPair.getRefreshTokenExpiresIn()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .success(false)
                            .message(String.format("Authentication failed. Reason: " + e.getMessage()))
                            .build());
        }
    }


    @GetMapping("/secure/auth/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            JwtTokenProvider.TokenInfo tokenInfo = jwtTokenProvider.validateTokenAndGetInfo(token);

            return ResponseEntity.ok().body(Map.of(
                    "valid", true,
                    "userId", tokenInfo.getUuid(),
                    "authorities", tokenInfo.getAuthorities(),
                    "remainingTime", jwtTokenProvider.getTokenRemainingTime(token)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "error", e.getMessage()));
        }
    }

    @GetMapping("/secure/auth/test-role")
    @ResponseBody
    public String testRole() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return "Roles: " + auth.getAuthorities();
    }
}
