package com.coop.voting.system.domain.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class VotingSessionUnavailableException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public VotingSessionUnavailableException(String message) {
        super(message);
    }
}