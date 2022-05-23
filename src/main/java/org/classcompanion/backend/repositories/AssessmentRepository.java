package org.classcompanion.backend.repositories;

import org.classcompanion.backend.models.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {
}
