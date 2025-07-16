package com.creditas.ce_auth_ms.service;

import com.creditas.ce_auth_ms.client.UserClient;
import com.creditas.ce_auth_ms.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserClient userClient;

    public User findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            log.warn("Tentativa de busca com email vazio ou nulo.");
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        User user = userClient.findByEmail(email).getBody();
        log.info("Usuário encontrado para o email: {}", email);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            log.warn("Tentativa de autenticação com username vazio ou nulo.");
            throw new UsernameNotFoundException("Username não pode ser vazio");
        }
        User user = userClient.findByEmail(username).getBody();
        log.info("Usuário autenticado: {}", username);
        return user;
    }
}