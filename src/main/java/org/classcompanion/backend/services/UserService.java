package org.classcompanion.backend.services;

import org.classcompanion.backend.models.ERole;
import org.classcompanion.backend.models.Role;
import org.classcompanion.backend.models.User;
import org.classcompanion.backend.payload.requests.UserUpdateRequest;
import org.classcompanion.backend.payload.responses.BasicResponse;
import org.classcompanion.backend.repositories.RoleRepository;
import org.classcompanion.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	public User getUser(Long id) {
		return userRepository.findById(id).get();
	}

	public List<User> listUsers() {
		return userRepository.findAll();
	}

	public ResponseEntity<?> updateUser(Long id, UserUpdateRequest request) {
		User user = userRepository.getById(id);
		user.setFullName(request.getFullName());
		user.setChatId(request.getChatId());

		switch(request.getRole()) {
			case "student":
				Optional<Role> studentRole = roleRepository.findByName(ERole.ROLE_STUDENT);
				user.getRoles().clear();
				user.getRoles().add(studentRole.get());
				break;

			case "priority":
				Optional<Role> priorityRole = roleRepository.findByName(ERole.ROLE_PRIORITY);
				user.getRoles().clear();
				user.getRoles().add(priorityRole.get());
				break;

			case "admin":
				Optional<Role> adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
				user.getRoles().clear();
				user.getRoles().add(adminRole.get());
				break;
			default:
				return ResponseEntity.badRequest().body(new BasicResponse(false, 400, "Invalid role."));
		}

		userRepository.saveAndFlush(user);
		return ResponseEntity.ok(new BasicResponse(true, 200, "User updated successfully."));
	}

	public List<User> getUnapprovedUsers() {
		return userRepository.findByRolesIsEmpty();
	}

	public List<User> getApprovedUsers() {
		return userRepository.findByRolesIsNotEmpty();
	}

	public BasicResponse approveUser(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);

		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			Optional<Role> studentRole = roleRepository.findByName(ERole.ROLE_STUDENT);

			user.getRoles().add(studentRole.get());
			userRepository.saveAndFlush(user);
		}

		return new BasicResponse(true, 200, "User approved successfully.");
	}

	public BasicResponse disapproveUser(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);

		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			Optional<Role> studentRole = roleRepository.findByName(ERole.ROLE_STUDENT);

			user.getRoles().remove(studentRole.get());
			userRepository.saveAndFlush(user);
		}

		return new BasicResponse(true, 200, "User disapproved successfully.");
	}
}
