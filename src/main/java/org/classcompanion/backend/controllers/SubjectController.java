package org.classcompanion.backend.controllers;

import org.classcompanion.backend.models.Subject;
import org.classcompanion.backend.payload.responses.BasicResponse;
import org.classcompanion.backend.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {
	@Autowired
	private SubjectService service;

	@GetMapping("/list")
	public List<Subject> listSubjects() {
		return service.listSubjects();
	}

	@GetMapping("/get/{id}")
	public Subject getSubject(@PathVariable Integer id) {
		return service.getSubject(id);
	}

	@PatchMapping("/update/{id}")
	public BasicResponse updateSubject(@PathVariable Integer id, @RequestBody Subject subject) {
		return service.updateSubject(id, subject);
	}

	@PostMapping("/add")
	public BasicResponse addSubject(@RequestBody Subject subject) {
		return service.addSubject(subject);
	}

	@DeleteMapping("/remove/{subjectId}")
	public BasicResponse removeSubject(@PathVariable Integer subjectId) {
		return service.removeSubject(subjectId);
	}
}
