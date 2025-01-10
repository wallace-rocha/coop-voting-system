package com.coop.voting.system.domain.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CpfInvalidException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public CpfInvalidException(String message) {
        super(message);
    }

}