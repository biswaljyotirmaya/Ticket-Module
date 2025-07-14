package com.ticket.controller;

import com.ticket.entity.Relationship;
import com.ticket.entity.Ticket;
import com.ticket.service.RelationshipService;
import com.ticket.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/relationships")
@CrossOrigin(origins = "http://localhost:4200")
public class RelationshipController {
    private final RelationshipService relationshipService;
    private final TicketService ticketService;

    public RelationshipController(RelationshipService relationshipService, TicketService ticketService) {
        this.relationshipService = relationshipService;
        this.ticketService = ticketService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Relationship> createRelationship(@Valid @RequestBody Relationship relationship) {
        Relationship savedRelationship = relationshipService.saveRelationship(relationship);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRelationship);
    }

    @GetMapping(value = "/by-source-ticket/{sourceTicketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Relationship>> getRelationshipsBySourceTicket(@PathVariable Long sourceTicketId) {
        List<Relationship> relationships = relationshipService.getRelationshipsBySourceTicket(sourceTicketId);
        return ResponseEntity.ok(relationships);
    }

    // You might also need a way to search for target tickets
    @GetMapping(value = "/search-tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ticket>> searchTickets(@RequestParam String query) {
        // This is a basic search, you might want a more sophisticated one in TicketService
        List<Ticket> tickets = ticketService.getAllTickets().stream()
                                .filter(t -> t.getTitleTicket().toLowerCase().contains(query.toLowerCase()) ||
                                             String.valueOf(t.getSno()).contains(query))
                                .limit(10) // Limit results
                                .toList();
        return ResponseEntity.ok(tickets);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable Long id) {
        relationshipService.deleteRelationship(id);
        return ResponseEntity.noContent().build();
    }
}