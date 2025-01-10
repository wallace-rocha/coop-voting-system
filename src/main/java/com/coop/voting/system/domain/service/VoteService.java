package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Agenda;
import com.coop.voting.system.domain.model.Member;
import com.coop.voting.system.domain.model.Vote;
import com.coop.voting.system.domain.model.exception.*;
import com.coop.voting.system.domain.repository.AgendaRepository;
import com.coop.voting.system.domain.repository.MemberRepository;
import com.coop.voting.system.domain.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VoteService {

    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Transactional
    public Vote castVote(UUID agendaId, String cpf, String choice) {
        logger.info("Starting vote process for agenda ID: {}, CPF: {}, Choice: {}", agendaId, cpf, choice);

        Agenda agenda = agendaRepository.findByAgendaId(agendaId)
                .orElseThrow(() -> {
                    logger.error("Agenda not found for ID: {}", agendaId);
                    return new AgendaNotFoundException("Agenda not found.");
                });

        choice = getChoiceFormatted(choice);
        validateFields(agenda, cpf);

        Vote vote = Vote.builder()
                .agenda(agenda)
                .cpf(cpf)
                .choice(choice.toUpperCase())
                .build();

        return voteRepository.save(vote);
    }

    protected String getChoiceFormatted(String choice) {
        logger.info("Formatting choice: {}", choice);

        choice = Normalizer.normalize(choice.toUpperCase(), Normalizer.Form.NFD);
        choice = choice.replaceAll("[^\\p{ASCII}]", "");

        if (!"SIM".equalsIgnoreCase(choice) && !"NAO".equalsIgnoreCase(choice)) {
            logger.warn("Invalid choice provided: {}", choice);
            throw new BusinessException("Invalid choice.");
        }
        return choice;
    }

    private void validateFields(Agenda agenda, String cpf) {
        logger.info("Validating fields for CPF: {}", cpf);

        checkOpenSession(agenda);
        checkMemberVoted(agenda, cpf);
    }

    private void checkOpenSession(Agenda agenda) {
        LocalDateTime now = LocalDateTime.now();
        if (agenda.getVotingStartDate() == null || agenda.getVotingEndDate() == null
                || now.isBefore(agenda.getVotingStartDate()) || now.isAfter(agenda.getVotingEndDate())) {
            throw new VotingSessionUnavailableException("Voting session is not open or has already ended.");
        }
        logger.info("Voting session is valid for agenda ID: {}", agenda.getAgendaId());
    }

    private void checkMemberVoted(Agenda agenda, String cpf) {
        Member member = memberRepository.findByCpf(cpf)
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));

        Optional<Vote> existingVote = voteRepository.findByAgendaAndCpf(agenda, cpf);
        if (existingVote.isPresent()) {
            throw new MemberAlreadyVotedException("Member already voted.");
        }
        logger.info("Member with CPF: {} has not voted yet on agenda ID: {}", cpf, agenda.getAgendaId());
    }
}