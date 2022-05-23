package org.classcompanion.backend.controllers;

import org.classcompanion.backend.models.User;
import org.classcompanion.backend.payload.requests.LoginRequest;
import org.classcompanion.backend.payload.requests.SignupRequest;
import org.classcompanion.backend.payload.responses.BasicResponse;
import org.classcompanion.backend.payload.responses.JwtResponse;
import org.classcompanion.backend.repositories.RoleRepository;
import org.classcompanion.backend.repositories.UserRepository;
import org.classcompanion.backend.security.jwt.JwtUtils;
import org.classcompanion.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(
						jwt,
						userDetails.getId(),
						userDetails.getUsername(),
						userDetails.getEmail(),
						roles
				)
		);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {
		if(userRepository.existsByUsername(request.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new BasicResponse(false, 400, "Username is already taken"));
		}

		if(userRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new BasicResponse(false, 400, "Email is already in use"));
		}

		User user = new User(
				request.getUsername(),
				request.getEmail(),
				request.getFullName(),
				encoder.encode(request.getPassword())
		);
		userRepository.save(user);

		return ResponseEntity.ok(new BasicResponse(true, 200, "User registered successfully"));
	}
}