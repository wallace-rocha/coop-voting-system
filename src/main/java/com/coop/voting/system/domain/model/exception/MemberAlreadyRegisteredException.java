package com.coop.voting.system.domain.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MemberAlreadyRegisteredException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public MemberAlreadyRegisteredException(String message) {
        super(message);
    }

}