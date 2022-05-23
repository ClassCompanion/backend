package org.classcompanion.backend.controllers;

import org.classcompanion.backend.models.User;
import org.classcompanion.backend.payload.requests.UserUpdateRequest;
import org.classcompanion.backend.payload.responses.BasicResponse;
import org.classcompanion.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping("/get/{id}")
	public User getUser(@PathVariable Long id) {
		return service.getUser(id);
	}

	@GetMapping("/list")
	public List<User> listUsers() {
		return service.listUsers();
	}

	@PatchMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
		return service.updateUser(id, request);
	}

	@GetMapping("/unapproved")
	public List<User> getUnapprovedUsers() {
		return service.getUnapprovedUsers();
	}

	@GetMapping("/approved")
	public List<User> getApprovedUsers() {
		return service.getApprovedUsers();
	}

	@PatchMapping("/approve/{id}")
	public BasicResponse approveUser(@PathVariable Long id) {
		return service.approveUser(id);
	}

	@PatchMapping("/disapprove/{id}")
	public BasicResponse disapproveUser(@PathVariable Long id) {
		return service.disapproveUser(id);
	}
}