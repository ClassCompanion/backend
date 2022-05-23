package org.classcompanion.backend.repositories;

import org.classcompanion.backend.models.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Integer> {
}