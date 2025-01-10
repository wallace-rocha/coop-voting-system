package com.coop.voting.system.domain.repository;

import com.coop.voting.system.domain.model.Agenda;
import com.coop.voting.system.domain.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

    List<Vote> findByAgenda(Agenda agenda);
    Optional<Vote> findByAgendaAndCpf(Agenda agenda, String cpf);
}