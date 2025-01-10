package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Agenda;
import com.coop.voting.system.domain.model.Member;
import com.coop.voting.system.domain.model.Vote;
import com.coop.voting.system.domain.model.exception.*;
import com.coop.voting.system.domain.repository.AgendaRepository;
import com.coop.voting.system.domain.repository.MemberRepository;
import com.coop.voting.system.domain.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private VoteService voteService;

    private final UUID agendaId = UUID.randomUUID();
    private final String validCpf = "12345678901";
    private final String validChoice = "SIM";
    private final Agenda agenda = Agenda.builder()
            .agendaId(agendaId)
            .subject("Agenda Subject")
            .inclusionDate(LocalDateTime.now())
            .votingStartDate(LocalDateTime.now().minusMinutes(10))
            .votingEndDate(LocalDateTime.now().plusMinutes(10))
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCastVote_Success() throws Exception {
        Member member = new Member(validCpf, "Wallace Rocha", LocalDateTime.now());
        Vote expectedVote = Vote.builder()
                .agenda(agenda)
                .cpf(validCpf)
                .choice("SIM")
                .build();

        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(agenda));
        when(memberRepository.findByCpf(validCpf)).thenReturn(Optional.of(member));
        when(voteRepository.findByAgendaAndCpf(agenda, validCpf)).thenReturn(Optional.empty());
        when(voteRepository.save(any(Vote.class))).thenReturn(expectedVote);

        Vote result = voteService.castVote(agendaId, validCpf, validChoice);

        assertNotNull(result);
        assertEquals(validCpf, result.getCpf());
        assertEquals("SIM", result.getChoice());
        assertEquals(agendaId, result.getAgenda().getAgendaId());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    void testCastVote_AgendaNotFound() {
        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.empty());

        AgendaNotFoundException exception = assertThrows(AgendaNotFoundException.class, () -> {
            voteService.castVote(agendaId, validCpf, validChoice);
        });

        assertEquals("Agenda not found.", exception.getMessage());
    }

    @Test
    void testCastVote_ChoiceInvalid() throws Exception {
        UUID agendaId = UUID.randomUUID();
        String validCpf = "12345678900";
        String invalidChoice = "TALVEZ";

        Agenda agenda = new Agenda();
        agenda.setAgendaId(agendaId);
        agenda.setSubject("Test Agenda");
        agenda.setInclusionDate(LocalDateTime.now());

        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(agenda));

        when(memberRepository.findByCpf(validCpf)).thenReturn(Optional.of(new Member(validCpf, "Wallace Rocha", LocalDateTime.now())));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            voteService.castVote(agendaId, validCpf, invalidChoice);
        });

        assertEquals("Invalid choice.", exception.getMessage());
    }


    @Test
    void testCastVote_VotingSessionUnavailable() {
        Agenda closedAgenda = Agenda.builder()
                .agendaId(agendaId)
                .subject("Closed Agenda")
                .inclusionDate(LocalDateTime.now())
                .votingStartDate(LocalDateTime.now().minusMinutes(20))
                .votingEndDate(LocalDateTime.now().minusMinutes(10))
                .build();

        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(closedAgenda));

        VotingSessionUnavailableException exception = assertThrows(VotingSessionUnavailableException.class, () -> {
            voteService.castVote(agendaId, validCpf, validChoice);
        });

        assertEquals("Voting session is not open or has already ended.", exception.getMessage());
    }

    @Test
    void testCastVote_MemberAlreadyVoted() {
        Member member = new Member(validCpf, "Wallace Rocha", LocalDateTime.now());
        Vote existingVote = Vote.builder()
                .agenda(agenda)
                .cpf(validCpf)
                .choice("SIM")
                .build();

        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(agenda));
        when(memberRepository.findByCpf(validCpf)).thenReturn(Optional.of(member));
        when(voteRepository.findByAgendaAndCpf(agenda, validCpf)).thenReturn(Optional.of(existingVote));

        MemberAlreadyVotedException exception = assertThrows(MemberAlreadyVotedException.class, () -> {
            voteService.castVote(agendaId, validCpf, validChoice);
        });

        assertEquals("Member already voted.", exception.getMessage());
    }

    @Test
    void testCastVote_MemberNotFound() {
        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(agenda));
        when(memberRepository.findByCpf(validCpf)).thenReturn(Optional.empty());

        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> {
            voteService.castVote(agendaId, validCpf, validChoice);
        });

        assertEquals("Member not found.", exception.getMessage());
    }

    @Test
    void testGetChoiceFormatted_InvalidChoice() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            voteService.getChoiceFormatted("TALVEZ");
        });

        assertEquals("Invalid choice.", exception.getMessage());
    }
}
