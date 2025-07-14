package com.ticket.controller;

import com.ticket.entity.Link;
import com.ticket.entity.Ticket;
import com.ticket.service.LinkService;
import com.ticket.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/links")
@CrossOrigin(origins = "http://localhost:4200")
public class LinkController {
    private final LinkService linkService;
    private final TicketService ticketService;

    public LinkController(LinkService linkService, TicketService ticketService) {
        this.linkService = linkService;
        this.ticketService = ticketService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Link> createLink(@Valid @RequestBody Link link) {
        // Ensure the parentTicket exists before saving the link
        if (link.getParentTicket() != null && link.getParentTicket().getSno() != null) {
            Optional<Ticket> parentTicket = ticketService.getTicketById(link.getParentTicket().getSno());
            if (parentTicket.isPresent()) {
                link.setParentTicket(parentTicket.get());
                Link savedLink = linkService.saveLink(link);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedLink);
            }
        }
        return ResponseEntity.badRequest().build(); // Or a more specific error
    }

    @GetMapping(value = "/by-ticket/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Link>> getLinksByTicket(@PathVariable Long ticketId) {
        List<Link> links = linkService.getLinksByParentTicket(ticketId);
        return ResponseEntity.ok(links);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        linkService.deleteLink(id);
        return ResponseEntity.noContent().build();
    }
}