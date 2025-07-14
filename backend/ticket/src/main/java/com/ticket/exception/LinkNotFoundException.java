package com.ticket.exception;

public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException(String message) {
        super(message);
    }
    public LinkNotFoundException(Long id) {
        super("Link not found with id: " + id);
    }
}