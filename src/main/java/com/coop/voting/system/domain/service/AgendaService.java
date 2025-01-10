package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Agenda;
import com.coop.voting.system.domain.model.Vote;
import com.coop.voting.system.domain.model.exception.AgendaNotFoundException;
import com.coop.voting.system.domain.model.exception.BusinessException;
import com.coop.voting.system.domain.model.exception.VotingNotClosedException;
import com.coop.voting.system.domain.model.exception.VotingNotOpenedException;
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
    public Agenda createAgenda(String subject) {
        if (subject.trim().isEmpty()) {
            throw new BusinessException("The request parameter cannot be empty.");
        }

        LocalDateTime now = LocalDateTime.now();
        Agenda agenda = Agenda.builder()
                .subject(subject)
                .inclusionDate(now)
                .build();

        return agendaRepository.save(agenda);
    }

    @Transactional
    public void openVotingSession(UUID agendaId, LocalDateTime votingStartDate, LocalDateTime votingEndDate) {
        Agenda agenda = agendaRepository.findByAgendaId(agendaId)
                .orElseThrow(() -> {
                    return new AgendaNotFoundException("Agenda not found.");
                });

        if (Objects.nonNull(agenda.getVotingStartDate()) || Objects.nonNull(agenda.getVotingEndDate())) {
            throw new BusinessException("The session has already been opened.");
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

    public String getVotingResult(UUID agendaId) {
        Agenda agenda = agendaRepository.findByAgendaId(agendaId)
                .orElseThrow(() -> {
                    return new AgendaNotFoundException("Agenda not found.");
                });

        if (Objects.isNull(agenda.getVotingEndDate())) {
            throw new VotingNotOpenedException("Voting session for the agenda has not been opened yet.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (Objects.isNull(agenda.getVotingEndDate()) || now.isBefore(agenda.getVotingEndDate())) {
            throw new VotingNotClosedException("Voting session is not closed yet.");
        }

        List<Vote> votes = voteRepository.findByAgenda(agenda);
        long yesVotes = votes.stream().filter(vote -> vote.getChoice().equalsIgnoreCase("SIM")).count();
        long noVotes = votes.stream().filter(vote -> vote.getChoice().equalsIgnoreCase("NAO")).count();

        return String.format("Sim: %d, Não: %d", yesVotes, noVotes);
    }
}