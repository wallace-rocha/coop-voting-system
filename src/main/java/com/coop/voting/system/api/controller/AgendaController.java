package com.coop.voting.system.api.controller;

import com.coop.voting.system.domain.model.Agenda;
import com.coop.voting.system.domain.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agenda createAgenda(@RequestParam String subject) throws Exception {
        try {
            Agenda agenda = agendaService.createAgenda(subject);
            return agenda;
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/{agendaId}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void openVotingSession(@PathVariable UUID agendaId,
                                  @RequestParam(required = false) LocalDateTime votingStartDate,
                                  @RequestParam(required = false) LocalDateTime votingEndDate) {
        try {
            agendaService.openVotingSession(agendaId, votingStartDate, votingEndDate);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/{agendaId}/result")
    @ResponseStatus(HttpStatus.OK)
    public String getVotingResult(@PathVariable UUID agendaId) {
        try {
            String result = agendaService.getVotingResult(agendaId);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
}