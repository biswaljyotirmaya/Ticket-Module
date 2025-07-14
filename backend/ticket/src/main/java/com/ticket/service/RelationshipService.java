package com.ticket.service;

import com.ticket.entity.Relationship;
import com.ticket.entity.Ticket;
import com.ticket.exception.RelationshipNotFoundException; // You'll need to create this exception
import com.ticket.repository.RelationshipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final TicketService ticketService; // To fetch source and target tickets

    public RelationshipService(RelationshipRepository relationshipRepository, TicketService ticketService) {
        this.relationshipRepository = relationshipRepository;
        this.ticketService = ticketService;
    }

    public Relationship saveRelationship(Relationship relationship) {
        // Ensure source and target tickets are loaded and associated
        if (relationship.getSourceTicket() != null && relationship.getSourceTicket().getSno() != null) {
            Ticket source = ticketService.getTicketById(relationship.getSourceTicket().getSno())
                            .orElseThrow(() -> new RelationshipNotFoundException("Source Ticket not found with ID: " + relationship.getSourceTicket().getSno()));
            relationship.setSourceTicket(source);
        }

        if (relationship.getTargetTicket() != null && relationship.getTargetTicket().getSno() != null) {
            Ticket target = ticketService.getTicketById(relationship.getTargetTicket().getSno())
                            .orElseThrow(() -> new RelationshipNotFoundException("Target Ticket not found with ID: " + relationship.getTargetTicket().getSno()));
            relationship.setTargetTicket(target);
            relationship.setTargetTicketTitle(target.getTitleTicket()); // Set title for easier display
        }
        return relationshipRepository.save(relationship);
    }

    public List<Relationship> getRelationshipsBySourceTicket(Long ticketId) {
        Ticket sourceTicket = ticketService.getTicketById(ticketId)
                                .orElseThrow(() -> new RelationshipNotFoundException("Source Ticket not found with ID: " + ticketId));
        return relationshipRepository.findBySourceTicket(sourceTicket);
    }

    public Optional<Relationship> getRelationshipById(Long id) {
        return relationshipRepository.findById(id);
    }

    public void deleteRelationship(Long id) {
        if (!relationshipRepository.existsById(id)) {
            throw new RelationshipNotFoundException("Relationship not found with ID: " + id);
        }
        relationshipRepository.deleteById(id);
    }
}