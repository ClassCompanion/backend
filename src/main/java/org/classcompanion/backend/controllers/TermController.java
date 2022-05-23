package org.classcompanion.backend.controllers;

import org.classcompanion.backend.models.Term;
import org.classcompanion.backend.payload.responses.BasicResponse;
import org.classcompanion.backend.services.TermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/term")
public class TermController {
	@Autowired
	private TermService service;

	@GetMapping("/get/{id}")
	public Term getTerm(@PathVariable Integer id) {
		return service.getTerm(id);
	}

	@PostMapping("/enroll/{id}")
	public BasicResponse entroll(@PathVariable Integer id, Principal principal) {
		return service.enroll(id, principal);
	}
}
