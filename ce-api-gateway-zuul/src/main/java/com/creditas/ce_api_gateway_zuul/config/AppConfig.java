package com.creditas.ce_api_gateway_zuul.config;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@RefreshScope
@Configuration
public class AppConfig {

    private static final String SECRET_KEY = "segredo-123";

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(
                new javax.crypto.spec.SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256")
        ).build();
    }
}