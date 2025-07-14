//package com.ticket.exception;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ticket.entity.Ticket;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.validation.BindException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(GlobalExceptionHandler.class)
//class GlobalExceptionHandlerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private GlobalExceptionHandler globalExceptionHandler;
//
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void testHandleValidationExceptions() {
//        // Given
//        FieldError fieldError1 = new FieldError("ticket", "titleTicket", "Title is required");
//        FieldError fieldError2 = new FieldError("ticket", "assign", "Assignee is required");
//        
//        BindException bindException = new BindException(new Object(), "ticket");
//        bindException.addError(fieldError1);
//        bindException.addError(fieldError2);
//        
//        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
//                null, bindException);
//
//        // When
//        var response = globalExceptionHandler.handleValidationExceptions(exception);
//
//        // Then
//        assertEquals(400, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals("Title is required", response.getBody().get("titleTicket"));
//        assertEquals("Assignee is required", response.getBody().get("assign"));
//    }
//
//    @Test
//    void testHandleTicketNotFoundException() {
//        // Given
//        TicketNotFoundException exception = new TicketNotFoundException("Ticket not found with id: 999");
//
//        // When
//        var response = globalExceptionHandler.handleTicketNotFoundException(exception);
//
//        // Then
//        assertEquals(404, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals("Ticket not found with id: 999", response.getBody().get("error"));
//    }
//
//    @Test
//    void testHandleRuntimeException() {
//        // Given
//        RuntimeException exception = new RuntimeException("Database connection failed");
//
//        // When
//        var response = globalExceptionHandler.handleRuntimeException(exception);
//
//        // Then
//        assertEquals(500, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals("Database connection failed", response.getBody().get("error"));
//    }
//
//    @Test
//    void testHandleGenericException() {
//        // Given
//        Exception exception = new Exception("Unexpected error");
//
//        // When
//        var response = globalExceptionHandler.handleGenericException(exception);
//
//        // Then
//        assertEquals(500, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals("An unexpected error occurred", response.getBody().get("error"));
//    }
//
//    @Test
//    void testHandleValidationExceptions_EmptyErrors() {
//        // Given
//        BindException bindException = new BindException(new Object(), "ticket");
//        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
//                null, bindException);
//
//        // When
//        var response = globalExceptionHandler.handleValidationExceptions(exception);
//
//        // Then
//        assertEquals(400, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().isEmpty());
//    }
//
//    @Test
//    void testHandleTicketNotFoundException_WithId() {
//        // Given
//        TicketNotFoundException exception = new TicketNotFoundException(123L);
//
//        // When
//        var response = globalExceptionHandler.handleTicketNotFoundException(exception);
//
//        // Then
//        assertEquals(404, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals("Ticket not found with id: 123", response.getBody().get("error"));
//    }
//} 



package com.ticket.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.entity.Ticket; // Not directly used in the handler test but might be present in a real scenario
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; // Remove if not performing MVC tests
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // Not directly used in the handler test
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.test.web.servlet.MockMvc; // Remove if not performing MVC tests

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
// Removed unnecessary MockMvc imports as this is a unit test of the handler itself, not through MockMvc calls
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GlobalExceptionHandler.class) // If you only test the handler methods directly, @WebMvcTest might be overkill. @ExtendWith(MockitoExtension.class) + direct calls would be simpler. Keeping it if your actual setup uses it.
class GlobalExceptionHandlerTest {

    @Autowired(required = false) // Make it optional as it might not be needed if not running MVC tests
    private MockMvc mockMvc;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    // ObjectMapper is not needed for unit testing the handler methods directly
    // private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // objectMapper = new ObjectMapper(); // Not needed if not using mockMvc for JSON content
        // Ensure the handler is instantiated, @Autowired should handle this in @WebMvcTest
        if (globalExceptionHandler == null) {
            globalExceptionHandler = new GlobalExceptionHandler();
        }
    }

    @Test
    void testHandleValidationExceptions() {
        // Given
        FieldError fieldError1 = new FieldError("ticket", "titleTicket", "Title is required");
        FieldError fieldError2 = new FieldError("ticket", "assign", "Assignee is required");
        
        BindException bindException = new BindException(new Object(), "ticket");
        bindException.addError(fieldError1);
        bindException.addError(fieldError2);
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                null, bindException);

        // When
        var response = globalExceptionHandler.handleValidationExceptions(exception);

        // Then
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Title is required", response.getBody().get("titleTicket"));
        assertEquals("Assignee is required", response.getBody().get("assign"));
    }

    @Test
    void testHandleTicketNotFoundException() {
        // Given
        // The handler now uses the ID constructor for TicketNotFoundException
        TicketNotFoundException exception = new TicketNotFoundException(999L); 

        // When
        var response = globalExceptionHandler.handleTicketNotFoundException(exception);

        // Then
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Ticket not found with id: 999", response.getBody().get("error"));
    }

    @Test
    void testHandleSubTicketNotFoundException() { // Added test for SubTicketNotFoundException
        // Given
        SubTicketNotFoundException exception = new SubTicketNotFoundException(123L);

        // When
        var response = globalExceptionHandler.handleSubTicketNotFoundException(exception);

        // Then
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("SubTicket not found with id: 123", response.getBody().get("error"));
    }

    @Test
    void testHandleLinkNotFoundException() { // Added test for LinkNotFoundException
        // Given
        LinkNotFoundException exception = new LinkNotFoundException(456L);

        // When
        var response = globalExceptionHandler.handleLinkNotFoundException(exception);

        // Then
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Link not found with id: 456", response.getBody().get("error"));
    }

    @Test
    void testHandleRelationshipNotFoundException() { // Added test for RelationshipNotFoundException
        // Given
        RelationshipNotFoundException exception = new RelationshipNotFoundException(789L);

        // When
        var response = globalExceptionHandler.handleRelationshipNotFoundException(exception);

        // Then
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Relationship not found with id: 789", response.getBody().get("error"));
    }

    @Test
    void testHandleGenericExceptionForRuntimeException() { // Renamed and clarified this test
        // Given
        RuntimeException exception = new RuntimeException("Database connection failed");

        // When
        var response = globalExceptionHandler.handleGenericException(exception); // Calls the correct method name

        // Then
        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        // The handler's generic message is "An unexpected error occurred"
        assertEquals("An unexpected error occurred", response.getBody().get("error")); 
    }
    
    // Removed the duplicate testHandleGenericException as it's covered by the renamed one above

    @Test
    void testHandleValidationExceptions_EmptyErrors() {
        // Given
        BindException bindException = new BindException(new Object(), "ticket");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                null, bindException);

        // When
        var response = globalExceptionHandler.handleValidationExceptions(exception);

        // Then
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty()); // Should be empty if no field errors
    }
}