package com.ticket.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.ticket.dto.TicketDTO;
import com.ticket.entity.Ticket;

public class TicketMapper {

    // Convert DTO to Entity
    public static Ticket toEntity(TicketDTO dto) {
        if (dto == null) return null;

        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        ticket.setTitleTicket(dto.getTitleTicket());
        ticket.setAssign(dto.getAssign());
        ticket.setStatus(dto.getStatus());
        ticket.setPriority(dto.getPriority());
        ticket.setDateCurrent(dto.getDateCurrent());
        ticket.setDescription(dto.getDescription());
        ticket.setPdfData(dto.getPdfData());
        ticket.setDocxData(dto.getDocxData());

        // Set parent ticket using only ID (to avoid infinite nesting)
        if (dto.getParentTicketId() != null) {
            Ticket parent = new Ticket();
            parent.setId(dto.getParentTicketId());
            ticket.setParentTicket(parent);
        }

        // Don't set subTickets in entity conversion — handled separately in service layer if needed
        return ticket;
    }

    // Convert Entity to DTO (with nested subTicket list)
    public static TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) return null;

        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTitleTicket(ticket.getTitleTicket());
        dto.setAssign(ticket.getAssign());
        dto.setStatus(ticket.getStatus());
        dto.setPriority(ticket.getPriority());
        dto.setDateCurrent(ticket.getDateCurrent());
        dto.setDescription(ticket.getDescription());
        dto.setPdfData(ticket.getPdfData());
        dto.setDocxData(ticket.getDocxData());

        if (ticket.getParentTicket() != null) {
            dto.setParentTicketId(ticket.getParentTicket().getId());
        }

        // Recursively convert subTickets into subTicketIds (List<TicketDTO>)
        if (ticket.getSubTickets() != null && !ticket.getSubTickets().isEmpty()) {
            List<TicketDTO> subDtos = ticket.getSubTickets().stream()
                .map(TicketMapper::toDTO)
                .collect(Collectors.toList());
            dto.setSubTicketIds(subDtos);
        } else {
            dto.setSubTicketIds(List.of());
        }

        return dto;
    }
}
