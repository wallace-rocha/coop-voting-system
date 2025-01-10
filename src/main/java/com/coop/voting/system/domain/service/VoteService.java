package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Agenda;
import com.coop.voting.system.domain.model.Member;
import com.coop.voting.system.domain.model.Vote;
import com.coop.voting.system.domain.repository.AgendaRepository;
import com.coop.voting.system.domain.repository.MemberRepository;
import com.coop.voting.system.domain.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Transactional
    public Vote castVote(UUID agendaId, String cpf, String choice) throws Exception {
        Agenda agenda = agendaRepository.findByAgendaId(agendaId)
                .orElseThrow(() -> {
                    return new Exception("Agenda not found.");
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

    protected String getChoiceFormatted(String choice) throws Exception {
        choice = Normalizer.normalize(choice.toUpperCase(), Normalizer.Form.NFD);
        choice = choice.replaceAll("[^\\p{ASCII}]", "");

        if (!"SIM".equalsIgnoreCase(choice) && !"NAO".equalsIgnoreCase(choice)) {
            throw new Exception("Invalid choice.");
        }
        return choice;
    }

    private void validateFields(Agenda agenda, String cpf) throws Exception {
        checkOpenSession(agenda);
        checkMemberVoted(agenda, cpf);
    }

    private void checkOpenSession(Agenda agenda) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        if (agenda.getVotingStartDate() == null || agenda.getVotingEndDate() == null
                || now.isBefore(agenda.getVotingStartDate()) || now.isAfter(agenda.getVotingEndDate())) {
            throw new Exception("Voting session is not open or has already ended.");
        }
    }

    private void checkMemberVoted(Agenda agenda, String cpf) throws Exception {
        Member member = memberRepository.findByCpf(cpf)
                .orElseThrow(() -> new Exception("Member not found."));

        Optional<Vote> existingVote = voteRepository.findByAgendaAndCpf(agenda, cpf);
        if (existingVote.isPresent()) {
            throw new Exception("Member already voted.");
        }
    }
}