package com.creditas.ce_auth_ms.service;

import com.creditas.ce_auth_ms.utils.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    private static final String SECRET_KEY = "segredo-123";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int EXPIRATION_TIME_MS = 86400000;

    public String generateToken(UserDetails user) {
        log.info("Generating JWT token for user: {}", user.getUsername());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        log.debug("Extracting username from token");
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isValidToken(String token, UserDetails user) {
        log.debug("Validating token for user: {}", user.getUsername());
        return extractUsername(token).equals(user.getUsername());
    }

    public OncePerRequestFilter jwtAuthFilter() {
        return new OncePerRequestFilter() {
            @Autowired
            private CustomUserDetailsService userService;

            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                log.info("Starting JWT authentication filter");
                String authHeader = getAuthorizationHeader(request);
                if (!isBearerToken(authHeader)) {
                    log.warn("Authorization header missing or does not start with Bearer");
                    filterChain.doFilter(request, response);
                    return;
                }

                String token = extractToken(authHeader);
                String username = extractUsernameSafely(token);
                if (shouldAuthenticate(username)) {
                    UserDetails userDetails = loadUserDetails(username);
                    if (isValidToken(token, userDetails)) {
                        setAuthentication(userDetails, request);
                        log.info("User {} authenticated successfully", username);
                    } else {
                        log.warn("Invalid token for user: {}", username);
                    }
                }
                filterChain.doFilter(request, response);
            }

            private String getAuthorizationHeader(HttpServletRequest request) {
                return request.getHeader("Authorization");
            }

            private boolean isBearerToken(String authHeader) {
                return authHeader != null && authHeader.startsWith(BEARER_PREFIX);
            }

            private String extractToken(String authHeader) {
                return authHeader.substring(BEARER_PREFIX.length());
            }

            private String extractUsernameSafely(String token) {
                try {
                    return extractUsername(token);
                } catch (Exception e) {
                    log.error("Failed to extract username from token", e);
                    return null;
                }
            }

            private boolean shouldAuthenticate(String username) {
                return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
            }

            private UserDetails loadUserDetails(String username) {
                log.debug("Loading user details for username: {}", username);
                return userService.loadUserByUsername(username);
            }

            private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        };
    }
}
