package org.classcompanion.extensions.eduimpl;

import org.opentimetable.javaottf.entities.Timetable;
import tk.thesuperlab.jea.JEA;

import java.util.Date;

public class EasistentImplementation implements EduImpl.Implementation {
	private final JEA jea;

	public EasistentImplementation(JEA jea) {
		this.jea = jea;
	}

	@Override
	public Timetable getTimetable(Date from, Date to) {
		return jea.getTimetable(from, to);
	}
}
