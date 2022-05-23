package org.classcompanion.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.classcompanion.backend.models.Assessment;
import org.classcompanion.backend.models.Term;
import org.classcompanion.backend.models.User;
import org.classcompanion.backend.payload.responses.BasicResponse;
import org.classcompanion.backend.repositories.TermRepository;
import org.classcompanion.backend.repositories.UserRepository;
import org.opentimetable.javaottf.entities.ClassEvent;
import org.opentimetable.javaottf.entities.Day;
import org.opentimetable.javaottf.entities.Timetable;
import org.opentimetable.javaottf.enums.WeekDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class TermService {
	@Autowired
	private TimetableService timetableService;
	@Autowired
	private AssessmentService assessmentService;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private UserRepository userRepository;

	public Term getTerm(Integer id) {
		return termRepository.findById(id).get();
	}

	public BasicResponse enroll(Integer termId, Principal principal) {
		Optional<Term> optionalTerm = termRepository.findById(termId);
		Optional<User> optionalUser = userRepository.findByUsername(principal.getName());

		if(optionalTerm.isPresent()) {
			Term term = optionalTerm.get();
			Assessment assessment = term.getAssessment();

			if(optionalUser.isPresent()) {
				User user = optionalUser.get();

				if(term.getUsers().size() < assessment.getPerDay()) {
					// Check if user is already enrolled
					Term enrolledTerm = assessmentService.isUserEnrolled(assessment, user);
					if(enrolledTerm != null) {
						enrolledTerm.getUsers().remove(user);
					}

					// Enroll user
					term.getUsers().add(user);
					termRepository.saveAndFlush(term);
				} else {
					return new BasicResponse(false, 400, "Term is full.");
				}

				assessmentService.sendAssessmentUpdate(assessmentService.getAssessment(assessment.getId()));

				return new BasicResponse(true, 200, "Entry added successfully.");
			} else {
				return new BasicResponse(false, 404, "User not found.");
			}
		} else {
			return new BasicResponse(false, 400, "Term not found.");
		}
	}

	public void generateTermsAndSave(Assessment assessment, int pupils) {
		Calendar cal = Calendar.getInstance();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");

		Date currentMonday = getDate(LocalDate.now().with(DayOfWeek.MONDAY));
		Date currentSunday = getDate(LocalDate.now().with(DayOfWeek.SUNDAY));

		int perDay = assessment.getPerDay();
		int numberOfDates = pupils / perDay + pupils % perDay;
		int i = 0;

		while(i < numberOfDates) {
			Timetable weekTimetable = timetableService.getTimetable(formatter.format(currentMonday), formatter.format(currentSunday));
			HashMap<WeekDay, Day> days = weekTimetable.getDays();

			for(Day day : days.values()) {
				TreeMap<String, List<ClassEvent>> dayClasses = day.getClasses();
				ArrayList<ClassEvent> classEvents = new ArrayList<ClassEvent>();

				for(List<ClassEvent> events : dayClasses.values()) {
					classEvents.addAll(events);
				}

				for(ClassEvent classEvent : classEvents) {
					if(classEvent.getAbbreviation().equalsIgnoreCase(assessment.getSubject().getAbbreviation()) && (!classEvent.isCanceled() && !classEvent.isExamination() && !classEvent.isSubstitution())) {
						Term term = new Term();
						term.setDate(java.sql.Date.valueOf(day.getDate()));
						term.setAssessment(assessment);

						termRepository.save(term);

						i++;
						break;
					}
				}

				if(i == numberOfDates) {
					break;
				}
			}

			cal.setTime(currentMonday);
			cal.add(Calendar.DATE, 7);
			currentMonday = cal.getTime();

			cal.setTime(currentSunday);
			cal.add(Calendar.DATE, 7);
			currentSunday = cal.getTime();
		}
	}

	private Date getDate(LocalDate localDate) {
		return Date.from(
				localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
		);
	}
}
