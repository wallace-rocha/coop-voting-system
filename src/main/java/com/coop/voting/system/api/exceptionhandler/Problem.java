package com.coop.voting.system.api.exceptionhandler;

import java.time.LocalDateTime;

public class Problem {

    String message;
    LocalDateTime dateTime;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}