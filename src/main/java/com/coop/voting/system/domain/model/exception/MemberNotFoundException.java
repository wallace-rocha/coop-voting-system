package com.coop.voting.system.domain.model.exception;

public class MemberNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public MemberNotFoundException(String message) {
        super(message);
    }

}