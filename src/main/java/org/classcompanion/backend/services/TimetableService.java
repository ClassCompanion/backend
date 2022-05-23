package org.classcompanion.backend.services;

import org.classcompanion.extensions.eduimpl.EasistentImplementation;
import org.classcompanion.extensions.eduimpl.EduImpl;
import org.classcompanion.extensions.eduimpl.LopolisImplementation;
import org.opentimetable.javaottf.entities.Timetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.thesuperlab.jea.JEA;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Service
public class TimetableService {
	private EduImpl.Implementation educationImplementation;

	@Value("${eduimpl.username}")
	private String implUsername;
	@Value("${eduimpl.password}")
	private String implPassword;
	@Value("${eduimpl.implementation}")
	private EduImpl eduImpl;

	@Autowired
	public void init() throws Exception {
		switch(eduImpl) {
			case EASISTENT:
				educationImplementation = new EasistentImplementation(new JEA(implUsername, implPassword));
				break;

			case LOPOLIS:
				educationImplementation = new LopolisImplementation();
				break;

			default:
				throw new Exception("Invalid implementation configuration.");
		}
	}

	public Timetable getTimetable(String from, String to) throws DateTimeParseException {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateFrom = LocalDate.parse(from, format);
		LocalDate dateTo = LocalDate.parse(to, format);

		return educationImplementation.getTimetable(getDate(dateFrom), getDate(dateTo));
	}

	private Date getDate(LocalDate localDate) {
		return Date.from(
				localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
		);
	}
}
