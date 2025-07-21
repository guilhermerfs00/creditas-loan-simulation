package com.creditas.ce_auth_ms.feignclients;

import com.creditas.ce_auth_ms.entities.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ce-user-ms", path = "/users")
public interface UserFeignClient {

    @GetMapping("/search")
    ResponseEntity<User> findByEmail(@RequestParam("email") String email);
}
