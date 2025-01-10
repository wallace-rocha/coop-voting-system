package com.coop.voting.system.api.controller;

import com.coop.voting.system.domain.model.Agenda;
import com.coop.voting.system.domain.service.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agenda")
public class AgendaController {

    private static final Logger logger = LoggerFactory.getLogger(AgendaController.class);

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agenda createAgenda(@RequestParam String subject) throws Exception {
        logger.info("Received request to create agenda with subject: {}", subject);
        try {
            Agenda agenda = agendaService.createAgenda(subject);
            logger.info("Agenda created successfully: {}", agenda);
            return agenda;
        } catch (Exception e) {
            logger.error("Error creating agenda with subject: {}", subject, e);
            throw e;
        }
    }

    @PutMapping("/{agendaId}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void openVotingSession(@PathVariable UUID agendaId,
                                  @RequestParam(required = false) LocalDateTime votingStartDate,
                                  @RequestParam(required = false) LocalDateTime votingEndDate) {
        logger.info("Received request to open voting session for agenda ID: {}", agendaId);
        try {
            agendaService.openVotingSession(agendaId, votingStartDate, votingEndDate);
            logger.info("Voting session opened successfully for agenda ID: {}", agendaId);
        } catch (Exception e) {
            logger.error("Error opening voting session for agenda ID: {}", agendaId, e);
            throw e;
        }
    }

    @GetMapping("/{agendaId}/result")
    @ResponseStatus(HttpStatus.OK)
    public String getVotingResult(@PathVariable UUID agendaId) {
        logger.info("Received request to get voting result for agenda ID: {}", agendaId);
        try {
            String result = agendaService.getVotingResult(agendaId);
            logger.info("Voting result for agenda ID {}: {}", agendaId, result);
            return result;
        } catch (Exception e) {
            logger.error("Error retrieving voting result for agenda ID: {}", agendaId, e);
            throw e;
        }
    }
}