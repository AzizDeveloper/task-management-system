package dev.aziz.taskmanagementsystem.repositories;

import dev.aziz.taskmanagementsystem.entities.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Confirmation findByActivationCode(String activationCode);
}
