package com.dat.plantbackend.filter;

import com.dat.plantbackend.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();


        String ctxPath = request.getContextPath();
        logger.info("URI: " + uri);
        logger.info("Context path: " + ctxPath);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authoritiesFromContext = authentication == null ? Collections.emptySet() : authentication.getAuthorities();
        boolean isAdmin = checkRole(authoritiesFromContext);

        if (uri.startsWith(ctxPath + "/api/secure") && !isAdmin) {
            String header = request.getHeader("Authorization");
            logger.info("Header: " + header);
            if (header == null || !header.startsWith("Bearer ")) {
                logger.warn("Missing or invalid Authorization header.");
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            String token = header.substring(7);
            logger.info("Token: " + token);

            try {

                JwtTokenProvider.TokenInfo tokenInfo = jwtTokenProvider.validateTokenAndGetInfo(token);
                UUID uuid = UUID.fromString(tokenInfo.getUuid().toString());
                List<SimpleGrantedAuthority> authorities = tokenInfo.getAuthorities();
                logger.info("Authorities: " + authorities);
                logger.info("UUID: " + uuid);

                if (!uuid.toString().isEmpty() && authorities != null && !authorities.isEmpty()) {
                    request.setAttribute("userId", uuid);
                    request.setAttribute("authorities", authorities);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uuid, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    logger.warn("Token invalid");
                    SecurityContextHolder.clearContext();
                    sendErrorResponse(response, "Invalid token: uuid is null.");
                    return;
                }

            } catch (Exception e) {
                logger.warn("Token invalid: " + e.getMessage());
                SecurityContextHolder.clearContext();
                sendErrorResponse(response, "Token không hợp lệ hoặc hết hạn: " + e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage)
            throws IOException {
        if (!response.isCommitted()) {
            logger.info("Sending error response: " + errorMessage);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
            response.getWriter().flush();
        } else {
            logger.warn("Response already committed, cannot send error: " + errorMessage);
        }
    }

    private boolean checkRole(Collection<? extends GrantedAuthority> authoritiesFromContext) {
        for (GrantedAuthority authority : authoritiesFromContext) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
