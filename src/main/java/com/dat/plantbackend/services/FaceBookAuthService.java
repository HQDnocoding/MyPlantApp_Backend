package com.dat.plantbackend.services;


import com.dat.plantbackend.dto.FacebookUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class FaceBookAuthService {
    private final RestTemplate restTemplate;

    public FaceBookAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${facebook.app.id}")
    private String appId;

    @Value("${facebook.app.secret}")
    private String appSecret;

    public Boolean verifyToken(String userAccessToken) {
        String url = String.format(
                "https://graph.facebook.com/debug_token?input_token=%s&access_token=%s|%s",
                userAccessToken, appId, appSecret
        );

        Map<String, Map<String,Object>> result= this.restTemplate.getForObject(url, Map.class);

        assert result != null;
        return result.get("data").get("app_id").toString().equals(appId);
    }

    public FacebookUserInfo getUserProfile(String userAccessToken) {
        String url = String.format(
                "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=%s",
                userAccessToken
        );
        Map<String, Object> result= this.restTemplate.getForObject(url, Map.class);
        assert result != null;
        return FacebookUserInfo.builder()
                .id(result.get("id").toString())
                .name(result.get("name").toString())
                .email(result.get("email").toString())
                .picture(result.get("picture"))
                .build();
    }

}
