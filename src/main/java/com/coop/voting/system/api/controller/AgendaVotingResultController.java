package com.coop.voting.system.api.controller;

import com.coop.voting.system.domain.service.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/voting-result")
public class AgendaVotingResultController {

    private static final Logger logger = LoggerFactory.getLogger(AgendaVotingResultController.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private AgendaService agendaService;

    @PostMapping("/{agendaId}")
    @ResponseStatus(HttpStatus.OK)
    public void sendVotingResult(@PathVariable UUID agendaId) {
        logger.info("Starting to send voting result for agenda {}", agendaId);
        try {
            String result = agendaService.getVotingResult(agendaId);
            kafkaTemplate.send("coop-voting-result", result);
            logger.info("Voting result successfully sent to Kafka.");
        } catch (Exception e) {
            logger.error("Error retrieving voting result for agenda ID: {}", agendaId, e);
            throw e;
        }
    }
}