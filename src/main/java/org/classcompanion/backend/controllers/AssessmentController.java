package org.classcompanion.backend.controllers;

import org.classcompanion.backend.models.Assessment;
import org.classcompanion.backend.payload.responses.BasicResponse;
import org.classcompanion.backend.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessment")
public class AssessmentController {
	@Autowired
	private AssessmentService service;

	@GetMapping("/get/{assessmentId}")
	public Assessment getAssessment(@PathVariable Integer assessmentId) {
		return service.getAssessment(assessmentId);
	}

	@GetMapping("/list/{subjectId}")
	public List<Assessment> listAssessments(@PathVariable Integer subjectId) {
		return service.listAssessments(subjectId);
	}

	@PostMapping("/add/{subjectId}")
	public BasicResponse addAssessment(@PathVariable Integer subjectId, @RequestBody Assessment assessment) {
		return service.addAssessment(subjectId, assessment);
	}

	@DeleteMapping("/remove/{assessmentId}")
	public BasicResponse removeAssessment(@PathVariable Integer assessmentId) {
		return service.removeAssessment(assessmentId);
	}
}