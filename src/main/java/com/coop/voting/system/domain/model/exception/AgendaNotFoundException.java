package com.coop.voting.system.domain.model.exception;

public class AgendaNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public AgendaNotFoundException(String message) {
        super(message);
    }

}