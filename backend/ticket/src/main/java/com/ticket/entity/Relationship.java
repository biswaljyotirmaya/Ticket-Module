package com.ticket.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "relationships")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Relationship type is required")
    private String type; // e.g., "Blocking", "Waiting On", "Related To"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_ticket_id")
    @JsonIgnore
    private Ticket sourceTicket; // The ticket this relationship originates from

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_ticket_id")
    private Ticket targetTicket; // The other ticket in the relationship

    private String targetTicketTitle; // Store target ticket title for easier display
}