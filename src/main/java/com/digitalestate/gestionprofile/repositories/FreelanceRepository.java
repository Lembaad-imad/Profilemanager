package com.digitalestate.gestionprofile.repositories;

import com.digitalestate.gestionprofile.dao.Freelance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreelanceRepository extends JpaRepository<Freelance, Long> {
    Optional<Freelance> findFreelanceByEmailIgnoreCase(String email);
}
