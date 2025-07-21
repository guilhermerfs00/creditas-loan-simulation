package com.creditas.ce_auth_ms.services;

import com.creditas.ce_auth_ms.entities.User;
import com.creditas.ce_auth_ms.feignclients.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * Busca um usuário pelo e-mail. Usado pelo endpoint /users/search.
     */
    public User findByEmail(String email) {
        User user = userFeignClient.findByEmail(email).getBody();
        if (user == null) {
            logger.error("Email not found: {}", email);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("Email found: {}", email);
        return user;
    }

    /**
     * Implementação obrigatória para autenticação OAuth2 com grant_type=password.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userFeignClient.findByEmail(username).getBody();
        if (user == null) {
            logger.error("Email not found: {}", username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("Email found: {}", username);
        return user;
    }
}
