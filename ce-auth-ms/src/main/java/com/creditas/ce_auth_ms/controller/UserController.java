package com.creditas.ce_auth_ms.controller;

import com.creditas.ce_auth_ms.entities.User;
import com.creditas.ce_auth_ms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/search")
	public ResponseEntity<User> findByEmail(@RequestParam("email") String email) {
		User user = service.findByEmail(email);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}
}
