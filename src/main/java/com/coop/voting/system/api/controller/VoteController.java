package com.coop.voting.system.api.controller;

import com.coop.voting.system.domain.model.Vote;
import com.coop.voting.system.domain.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vote")
public class VoteController {

    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

    @Autowired
    private VoteService voteService;

    @PostMapping("/{agendaId}")
    @ResponseStatus(HttpStatus.OK)
    public Vote castVote(@PathVariable UUID agendaId, @RequestParam String cpf, @RequestParam String choice) throws Exception {
        logger.info("Received request to cast vote on agenda ID: {}, CPF: {}, Choice: {}", agendaId, cpf, choice);
        try {
            Vote vote = voteService.castVote(agendaId, cpf, choice);
            logger.info("Vote cast successfully: {}", vote);
            return vote;
        } catch (Exception e) {
            logger.error("Error casting vote on agenda ID: {}, CPF: {}", agendaId, cpf, e);
            throw e;
        }
    }
}