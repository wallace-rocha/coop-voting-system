package com.coop.voting.system.api.controller;

import com.coop.voting.system.domain.model.Vote;
import com.coop.voting.system.domain.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/{agendaId}")
    @ResponseStatus(HttpStatus.OK)
    public Vote castVote(@PathVariable UUID agendaId, @RequestParam String cpf, @RequestParam String choice) throws Exception {
        try {
            Vote vote = voteService.castVote(agendaId, cpf, choice);
            return vote;
        } catch (Exception e) {
            throw e;
        }
    }
}