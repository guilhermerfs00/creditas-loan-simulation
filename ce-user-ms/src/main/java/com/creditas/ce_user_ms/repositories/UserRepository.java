package com.creditas.ce_user_ms.repositories;


import com.creditas.ce_user_ms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
