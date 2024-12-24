package com.digitalestate.gestionprofile.repositories;

import com.digitalestate.gestionprofile.dao.Ens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnsRepository extends JpaRepository<Ens, Long> {
    Optional<Ens> findByEmailIgnoreCase(String email);
}
