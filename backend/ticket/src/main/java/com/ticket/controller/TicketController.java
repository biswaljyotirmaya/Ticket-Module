package com.ticket.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.dto.TicketDTO;
import com.ticket.exception.TicketNotFoundException;
import com.ticket.service.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        TicketDTO savedTicket = ticketService.saveTicket(ticketDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
    }

    @PostMapping("/{parentId}/subtask")
    public ResponseEntity<TicketDTO> createSubTicket(@PathVariable Long parentId, @Valid @RequestBody TicketDTO subTicketDTO) {
        // Fetch parent ticket DTO, throw if not found
        TicketDTO parentTicket = ticketService.getTicketById(parentId)
                .orElseThrow(() -> new TicketNotFoundException(parentId));
        subTicketDTO.setParentTicketId(parentTicket.getId());
        TicketDTO savedSubTicket = ticketService.saveTicket(subTicketDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubTicket);
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getRootTickets() {
        List<TicketDTO> dtoList = ticketService.getRootTickets();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        TicketDTO ticket = ticketService.getTicketById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketDTO ticketDTO) {
        TicketDTO updated = ticketService.updateTicket(id, ticketDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TicketDTO> patchTicketDescription(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        String description = updates.get("description");
        if (description == null) return ResponseEntity.badRequest().build();
        TicketDTO updated = ticketService.updateTicketDescription(id, description);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdfBytes = ticketService.getPdfDataById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ticket_" + id + ".pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}/download-docx")
    public ResponseEntity<byte[]> downloadDocx(@PathVariable Long id) {
        byte[] docxBytes = ticketService.getDocxDataById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "ticket_" + id + ".docx");
        return new ResponseEntity<>(docxBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}/subtickets")
    public ResponseEntity<List<TicketDTO>> getSubTickets(@PathVariable Long id) {
        List<TicketDTO> subTickets = ticketService.getSubTicketsByParent(id);
        return ResponseEntity.ok(subTickets);
    }
}
