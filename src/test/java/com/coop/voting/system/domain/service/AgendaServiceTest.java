package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Agenda;
import com.coop.voting.system.domain.model.Vote;
import com.coop.voting.system.domain.model.exception.AgendaNotFoundException;
import com.coop.voting.system.domain.model.exception.BusinessException;
import com.coop.voting.system.domain.model.exception.VotingNotClosedException;
import com.coop.voting.system.domain.model.exception.VotingNotOpenedException;
import com.coop.voting.system.domain.repository.AgendaRepository;
import com.coop.voting.system.domain.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private AgendaService agendaService;

    private final UUID agendaId = UUID.randomUUID();
    private final String validSubject = "New Agenda Subject";
    private final String emptySubject = " ";
    private final Agenda agenda = Agenda.builder()
            .agendaId(agendaId)
            .subject(validSubject)
            .inclusionDate(LocalDateTime.now())
            .votingStartDate(LocalDateTime.now().minusMinutes(10))
            .votingEndDate(LocalDateTime.now().plusMinutes(10))
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAgenda_Success() throws Exception {
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);

        Agenda result = agendaService.createAgenda(validSubject);

        assertNotNull(result);
        assertEquals(validSubject, result.getSubject());
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void testCreateAgenda_ThrowsBusinessException() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            agendaService.createAgenda(emptySubject);
        });

        assertEquals("The request parameter cannot be empty.", exception.getMessage());
    }

    @Test
    void testOpenVotingSession_Success() {
        LocalDateTime votingStartDate = LocalDateTime.now().plusMinutes(10);
        LocalDateTime votingEndDate = LocalDateTime.now().plusMinutes(20);

        agenda.setVotingStartDate(LocalDateTime.now().minusMinutes(10));
        agenda.setVotingEndDate(LocalDateTime.now().plusMinutes(10));

        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(agenda));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            agendaService.openVotingSession(agendaId, votingStartDate, votingEndDate);
        });

        assertEquals("The session has already been opened.", exception.getMessage());
    }

    @Test
    void testOpenVotingSession_AgendaNotFound() {
        LocalDateTime votingStartDate = LocalDateTime.now().plusMinutes(10);
        LocalDateTime votingEndDate = LocalDateTime.now().plusMinutes(20);

        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.empty());

        AgendaNotFoundException exception = assertThrows(AgendaNotFoundException.class, () -> {
            agendaService.openVotingSession(agendaId, votingStartDate, votingEndDate);
        });

        assertEquals("Agenda not found.", exception.getMessage());
    }

    @Test
    void testGetVotingResult_Success() {
        Vote voteOne = Vote.builder()
                .cpf("12345678901")
                .choice("SIM")
                .agenda(agenda)
                .build();

        Vote voteTwo = Vote.builder()
                .cpf("12345678902")
                .choice("NAO")
                .agenda(agenda)
                .build();

        List<Vote> votes = Arrays.asList(voteOne, voteTwo);

        agenda.setVotingEndDate(LocalDateTime.now().minusMinutes(5));
        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(agenda));
        when(voteRepository.findByAgenda(agenda)).thenReturn(votes);

        String result = agendaService.getVotingResult(agendaId);

        assertEquals("Sim: 1, Não: 1", result);
    }

    @Test
    void testGetVotingResult_AgendaNotFound() {
        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.empty());

        AgendaNotFoundException exception = assertThrows(AgendaNotFoundException.class, () -> {
            agendaService.getVotingResult(agendaId);
        });

        assertEquals("Agenda not found.", exception.getMessage());
    }

    @Test
    void testGetVotingResult_VotingNotClosed() {
        agenda.setVotingEndDate(LocalDateTime.now().plusMinutes(30));

        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(agenda));

        VotingNotClosedException exception = assertThrows(VotingNotClosedException.class, () -> {
            agendaService.getVotingResult(agendaId);
        });

        assertEquals("Voting session is not closed yet.", exception.getMessage());
    }

    @Test
    void testGetVotingResult_VotingNotOpened() {
        agenda.setVotingEndDate(null);

        when(agendaRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(agenda));

        VotingNotOpenedException exception = assertThrows(VotingNotOpenedException.class, () -> {
            agendaService.getVotingResult(agendaId);
        });

        assertEquals("Voting session for the agenda has not been opened yet.", exception.getMessage());
    }

}
