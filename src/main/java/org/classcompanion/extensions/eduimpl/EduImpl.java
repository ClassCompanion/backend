package org.classcompanion.extensions.eduimpl;

import org.opentimetable.javaottf.entities.Timetable;

import java.util.Date;

public enum EduImpl {
	EASISTENT,
	LOPOLIS;

	public interface Implementation {
		/**
		 * Method returns Timetable in a OTTF format
		 *
		 * @param from first day of the week
		 * @param to   last day of the week
		 * @return OTTF timetable
		 * @author chocoearly44
		 * @since 1.0
		 */
		Timetable getTimetable(Date from, Date to);
	}
}
