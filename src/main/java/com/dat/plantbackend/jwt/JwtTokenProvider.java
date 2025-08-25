package com.dat.plantbackend.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpirationInMs;

    @AllArgsConstructor
    @Getter
    public static class TokenInfo {
        private UUID uuid;
        private List<SimpleGrantedAuthority> authorities;
        private Date expirationTime;
        private String tokenType;
    }

    @AllArgsConstructor
    @Getter
    public static class TokenPair {
        private String accessToken;
        private String refreshToken;
        private long accessTokenExpiresIn;
        private long refreshTokenExpiresIn;
    }

    /**
     * Tạo cả access token và refresh token
     */
    public TokenPair generateTokenPair(UUID uuid, List<String> roles) throws Exception {
        String accessToken = generateAccessToken(uuid, roles);
        String refreshToken = generateRefreshToken(uuid);

        return new TokenPair(
                accessToken,
                refreshToken,
                jwtExpirationInMs,
                refreshTokenExpirationInMs
        );
    }

    /**
     * Tạo access token
     */
    public String generateAccessToken(UUID uuid, List<String> roles) throws Exception {
        JWSSigner signer = new MACSigner(jwtSecret);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(uuid))
                .claim("roles", roles)
                .claim("type", "access")
                .expirationTime(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    /**
     * Tạo refresh token
     */
    public String generateRefreshToken(UUID uuid) throws Exception {
        JWSSigner signer = new MACSigner(jwtSecret);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(uuid))
                .claim("type", "refresh")
                .expirationTime(new Date(System.currentTimeMillis() + refreshTokenExpirationInMs))
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    /**
     * Validate token và lấy thông tin
     */
    public TokenInfo validateTokenAndGetInfo(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(jwtSecret);

        if (!signedJWT.verify(verifier)) {
            throw new Exception("Invalid token signature");
        }

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        Date expiration = claims.getExpirationTime();
        System.out.println("expiration: " + expiration);
        if (expiration == null || expiration.before(new Date())) {
            throw new Exception("Token expired");
        }

        String uuid = claims.getSubject();
        String tokenType = claims.getStringClaim("type");

        List<SimpleGrantedAuthority> authorities = null;
        if ("access".equals(tokenType)) {
            List<String> roles = claims.getStringListClaim("roles");
            if (roles != null) {
                authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
        }

        return new TokenInfo(
                UUID.fromString(uuid),
                authorities,
                expiration,
                tokenType
        );
    }

    /**
     * Kiểm tra token có hết hạn không
     */
    public boolean isTokenExpired(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expiration == null || expiration.before(new Date());
        } catch (Exception e) {
            return true; // Nếu parse lỗi thì coi như expired
        }
    }

    /**
     * Kiểm tra token có hết hạn trong thời gian cho trước không
     */
    public boolean isTokenExpiringWithin(String token, long secondsFromNow) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiration == null) {
                return true;
            }

            Date checkTime = new Date(System.currentTimeMillis() + (secondsFromNow * 1000));
            return expiration.before(checkTime);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Lấy thời gian còn lại của token (tính bằng giây)
     */
    public long getTokenRemainingTime(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiration == null) {
                return 0;
            }

            long remaining = expiration.getTime() - System.currentTimeMillis();
            return Math.max(0, remaining / 1000);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Validate refresh token và tạo access token mới
     */
    public String refreshAccessToken(String refreshToken, List<String> roles) throws Exception {
        TokenInfo refreshTokenInfo = validateTokenAndGetInfo(refreshToken);

        // Kiểm tra xem có phải refresh token không
        if (!"refresh".equals(refreshTokenInfo.getTokenType())) {
            throw new Exception("Invalid token type. Expected refresh token");
        }

        // Tạo access token mới
        return generateAccessToken(refreshTokenInfo.getUuid(), roles);
    }

    /**
     * Validate refresh token và tạo cả access token và refresh token mới
     */
    public TokenPair refreshTokenPair(String refreshToken, List<String> roles) throws Exception {
        TokenInfo refreshTokenInfo = validateTokenAndGetInfo(refreshToken);

        // Kiểm tra xem có phải refresh token không
        if (!"refresh".equals(refreshTokenInfo.getTokenType())) {
            throw new Exception("Invalid token type. Expected refresh token");
        }

        // Tạo cả access token và refresh token mới
        return generateTokenPair(refreshTokenInfo.getUuid(), roles);
    }

    /**
     * Lấy UUID từ token mà không validate expiration
     */
    public UUID getUuidFromToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(jwtSecret);

        if (!signedJWT.verify(verifier)) {
            throw new Exception("Invalid token signature");
        }

        String uuid = signedJWT.getJWTClaimsSet().getSubject();
        return UUID.fromString(uuid);
    }

    /**
     * Kiểm tra token type
     */
    public String getTokenType(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getStringClaim("type");
    }
}