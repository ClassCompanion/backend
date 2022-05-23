package org.classcompanion.extensions.eduimpl;

import org.hibernate.cfg.NotYetImplementedException;
import org.opentimetable.javaottf.entities.Timetable;

import java.util.Date;

public class LopolisImplementation implements EduImpl.Implementation {
	@Override
	public Timetable getTimetable(Date from, Date to) {
		throw new NotYetImplementedException("Lopolis education implementation is not implemented yet.");
	}
}
