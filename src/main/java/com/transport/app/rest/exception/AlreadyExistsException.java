package com.transport.app.rest.exception;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends TransportAppException {

    private Long id;
    public AlreadyExistsException(String email) {
        super(email + " is not available.");
        this.id = id;
    }
}
