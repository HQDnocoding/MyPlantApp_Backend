package com.dat.plantbackend.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.provisioning.Account;
import com.cloudinary.utils.ObjectUtils;
import com.dat.plantbackend.common.CustomAuthFailureHandler;
import com.dat.plantbackend.common.Types;
import com.dat.plantbackend.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableAsync
@ComponentScan(basePackages = {
        "com.dat.plantbackend.controllers",
        "com.dat.plantbackend.services",
        "com.dat.plantbackend.repositories",
        "com.dat.plantbackend.filter"
})
public class SecurityConfig {

    @Value("${cloud.api.key}")
    private String apiKey;
    @Value("${cloud.api.secret}")
    private String apiSecret;
    @Value("${cloud.name}")
    private String cloudName;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    @Order(0)
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/secure/**").authenticated()
                        .requestMatchers("/api").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/secure/pesticides").hasRole(Types.Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE,"/api/secure/treatments").hasRole(Types.Roles.ADMIN.name())
                        .requestMatchers("/pesticides").hasRole(Types.Roles.ADMIN.name())
                        .requestMatchers("/treatments").hasRole(Types.Roles.ADMIN.name())
                        .anyRequest().permitAll()
                )

                .formLogin(form ->
                        form.loginPage("/login")
                                .loginProcessingUrl("/login").
                                defaultSuccessUrl("/", true)
                                .failureHandler(new CustomAuthFailureHandler())
                                .permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));

        return http.build();
    }


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }
}
