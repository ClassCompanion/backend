package org.classcompanion.backend.controllers;

import org.classcompanion.backend.services.TimetableService;
import org.opentimetable.javaottf.entities.Timetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timetable")
public class TimetableController {
	@Autowired
	private TimetableService service;

	@GetMapping("/get/{from}/{to}")
	public Timetable getTimetable(@PathVariable("from") String from, @PathVariable String to) {
		return service.getTimetable(from, to);
	}
}
