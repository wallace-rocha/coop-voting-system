package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Agenda;
import com.coop.voting.system.domain.model.Vote;
import com.coop.voting.system.domain.repository.AgendaRepository;
import com.coop.voting.system.domain.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Transactional
    public Agenda createAgenda(String subject) throws Exception {
        if (subject.trim().isEmpty()) {
            throw new Exception("The request parameter cannot be empty.");
        }

        LocalDateTime now = LocalDateTime.now();
        Agenda agenda = Agenda.builder()
                .subject(subject)
                .inclusionDate(now)
                .build();

        return agendaRepository.save(agenda);
    }

    @Transactional
    public void openVotingSession(UUID agendaId, LocalDateTime votingStartDate, LocalDateTime votingEndDate) throws Exception {
        Agenda agenda = agendaRepository.findByAgendaId(agendaId)
                .orElseThrow(() -> {
                    return new Exception("Agenda not found.");
                });

        if (Objects.nonNull(agenda.getVotingStartDate()) || Objects.nonNull(agenda.getVotingEndDate())) {
            throw new Exception("The session has already been opened.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (isNull(votingStartDate) || isNull(votingEndDate)) {
            votingStartDate = now;
            votingEndDate = now.plusMinutes(1);
        }

        agenda.setVotingStartDate(votingStartDate);
        agenda.setVotingEndDate(votingEndDate);
        agendaRepository.save(agenda);
    }

    public String getVotingResult(UUID agendaId) throws Exception {
        Agenda agenda = agendaRepository.findByAgendaId(agendaId)
                .orElseThrow(() -> {
                    return new Exception("Agenda not found.");
                });

        if (Objects.isNull(agenda.getVotingEndDate())) {
            throw new Exception("Voting session for the agenda has not been opened yet.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (Objects.isNull(agenda.getVotingEndDate()) || now.isBefore(agenda.getVotingEndDate())) {
            throw new Exception("Voting session is not closed yet.");
        }

        List<Vote> votes = voteRepository.findByAgenda(agenda);
        long yesVotes = votes.stream().filter(vote -> vote.getChoice().equalsIgnoreCase("SIM")).count();
        long noVotes = votes.stream().filter(vote -> vote.getChoice().equalsIgnoreCase("NAO")).count();

        return String.format("Sim: %d, Não: %d", yesVotes, noVotes);
    }
}