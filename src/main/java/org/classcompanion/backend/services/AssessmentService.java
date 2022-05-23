package org.classcompanion.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.classcompanion.backend.models.*;
import org.classcompanion.backend.payload.responses.BasicResponse;
import org.classcompanion.backend.repositories.AssessmentRepository;
import org.classcompanion.backend.repositories.SubjectRepository;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssessmentService {
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private AssessmentRepository assessmentRepository;
	@Autowired
	private TermService termService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper om;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Bean
	public Queue assessmentQueue() {
		return new Queue("s:assessments", false, false, false);
	}

	public Assessment getAssessment(Integer id) {
		return assessmentRepository.findById(id).get();
	}

	public List<Assessment> listAssessments(Integer subjectId) {
		Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

		if(optionalSubject.isPresent()) {
			Subject subject = optionalSubject.get();
			return subject.getAssessments();
		} else {
			return null;
		}
	}

	public BasicResponse addAssessment(Integer subjectId, Assessment assessment) {
		Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

		if(optionalSubject.isPresent()) {
			Subject subject = optionalSubject.get();
			assessment.setSubject(subject);

			try {
				assessmentRepository.save(assessment);
			} catch(Exception e) {
				return new BasicResponse(false, 500, e.getMessage());
			}

			termService.generateTermsAndSave(assessment, userService.getApprovedUsers().size());

			return new BasicResponse(true, 200, "Assessment added successfully.");
		} else {
			return new BasicResponse(false, 200, "Subject not found.");
		}
	}

	public BasicResponse removeAssessment(Integer assessmentId) {
		assessmentRepository.deleteById(assessmentId);
		return new BasicResponse(true, 200, "Assessment deleted successfully.");
	}

	public Term isUserEnrolled(Assessment assessment, User user) {
		for(Term termToCheck : assessment.getTerms()) {
			if(termToCheck.getUsers().contains(user)) {
				return termToCheck;
			}
		}

		return null;
	}

	// RabbitMQ
	public void sendAssessmentUpdate(Assessment assessment) {
		try {
			rabbitTemplate.convertAndSend("s:assessments", om.writeValueAsString(assessment));
		} catch(JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@RabbitListener(queues = "b:messageid")
	public void getAssessmentUpdate(String rawUpdate) {
		try {
			AssessmentUpdate update = om.readValue(rawUpdate, AssessmentUpdate.class);
			Optional<Assessment> optionalAssessment = assessmentRepository.findById(update.getAssessmentId());

			if(optionalAssessment.isPresent()) {
				Assessment assessment = optionalAssessment.get();
				assessment.setMessageId(update.getMessageId());

				assessmentRepository.save(assessment);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
