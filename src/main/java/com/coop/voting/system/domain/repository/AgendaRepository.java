package com.coop.voting.system.domain.repository;

import com.coop.voting.system.domain.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, UUID> {

    Optional<Agenda> findByAgendaId(UUID agendaId);
}