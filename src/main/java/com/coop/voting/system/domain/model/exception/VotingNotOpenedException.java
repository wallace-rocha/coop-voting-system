package com.coop.voting.system.domain.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class VotingNotOpenedException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public VotingNotOpenedException(String message) {
        super(message);
    }
}