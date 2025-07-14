package com.ticket.exception;

public class RelationshipNotFoundException extends RuntimeException {
    public RelationshipNotFoundException(String message) {
        super(message);
    }
    public RelationshipNotFoundException(Long id) {
        super("Relationship not found with id: " + id);
    }
}