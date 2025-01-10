package com.coop.voting.system.domain.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MemberAlreadyVotedException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public MemberAlreadyVotedException(String message) {
        super(message);
    }

}