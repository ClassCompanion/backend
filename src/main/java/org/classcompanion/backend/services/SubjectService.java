package org.classcompanion.backend.services;

import org.classcompanion.backend.models.Subject;
import org.classcompanion.backend.payload.responses.BasicResponse;
import org.classcompanion.backend.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
	@Autowired
	private SubjectRepository subjectRepository;

	public Subject getSubject(Integer id) {
		return subjectRepository.findById(id).get();
	}

	public BasicResponse updateSubject(Integer subjectId, Subject subject) {
		subject.setId(subjectId);
		subjectRepository.saveAndFlush(subject);
		return new BasicResponse(true, 200, "Subject updated successfully.");
	}

	public BasicResponse addSubject(Subject subject) {
		try {
			subjectRepository.saveAndFlush(subject);
		} catch(Exception e) {
			return new BasicResponse(false, 500, e.getMessage());
		}

		return new BasicResponse(true, 200, "Subject added successfully.");
	}

	public List<Subject> listSubjects() {
		return subjectRepository.findAll();
	}

	public BasicResponse removeSubject(Integer subjectId) {
		subjectRepository.deleteById(subjectId);
		return new BasicResponse(true, 200, "Subject deleted successfully.");
	}
}
