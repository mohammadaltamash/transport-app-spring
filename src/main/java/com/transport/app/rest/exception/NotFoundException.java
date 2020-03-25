package com.transport.app.rest.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends TransportAppException {

    private Long id;
    public NotFoundException(Class clazz, Long id) {
        super(clazz.getSimpleName() + " with id " + id + " not found.");
        this.id = id;
    }
    public NotFoundException(Class clazz, String email) {
        super(clazz.getSimpleName() + " with email " + email + " not found.");
        this.id = id;
    }
}
