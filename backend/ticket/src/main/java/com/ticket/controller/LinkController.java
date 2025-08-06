package com.ticket.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ticket.dto.LinkDTO;
import com.ticket.service.LinkService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/links")
@CrossOrigin(origins = "http://localhost:4200")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    public ResponseEntity<LinkDTO> createLink(@Valid @RequestBody LinkDTO linkDTO) {
        LinkDTO savedLink = linkService.saveLink(linkDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLink);
    }

    @GetMapping("/by-ticket/{ticketId}")
    public ResponseEntity<List<LinkDTO>> getLinksByTicket(@PathVariable Long ticketId) {
        List<LinkDTO> dtoList = linkService.getLinksByParentTicket(ticketId);
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        linkService.deleteLink(id);
        return ResponseEntity.noContent().build();
    }
}
