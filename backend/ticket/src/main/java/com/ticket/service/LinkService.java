package com.ticket.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticket.dto.LinkDTO;
import com.ticket.entity.Link;
import com.ticket.entity.Ticket;
import com.ticket.exception.LinkNotFoundException;
import com.ticket.mapper.LinkMapper;
import com.ticket.repository.LinkRepository;
import com.ticket.repository.TicketRepository;

@Service
@Transactional
public class LinkService {

    private final LinkRepository linkRepository;
    private final TicketRepository ticketRepository;

    public LinkService(LinkRepository linkRepository, TicketRepository ticketRepository) {
        this.linkRepository = linkRepository;
        this.ticketRepository = ticketRepository;
    }

    public LinkDTO saveLink(LinkDTO dto) {
        Link link = LinkMapper.toEntity(dto);
        if (dto.getParentTicketId() != null) {
            Ticket parent = ticketRepository.findById(dto.getParentTicketId())
                    .orElseThrow(() -> new LinkNotFoundException("Parent Ticket not found with ID: " + dto.getParentTicketId()));
            link.setParentTicket(parent);
        }
        Link saved = linkRepository.save(link);
        return LinkMapper.toDTO(saved);
    }

    public List<LinkDTO> getLinksByParentTicket(Long ticketId) {
        Ticket parentTicket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new LinkNotFoundException("Parent Ticket not found with ID: " + ticketId));
        return linkRepository.findByParentTicket(parentTicket)
                .stream()
                .map(LinkMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<LinkDTO> getLinkById(Long id) {
        return linkRepository.findById(id).map(LinkMapper::toDTO);
    }

    public void deleteLink(Long id) {
        if (!linkRepository.existsById(id)) {
            throw new LinkNotFoundException("Link not found with ID: " + id);
        }
        linkRepository.deleteById(id);
    }
}
