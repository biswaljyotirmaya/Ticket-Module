package com.ticket.service;


import com.ticket.entity.Link;
import com.ticket.entity.Ticket;
import com.ticket.exception.LinkNotFoundException; // You'll need to create this exception
import com.ticket.repository.LinkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LinkService {
    private final LinkRepository linkRepository;
    private final TicketService ticketService; // To fetch parent ticket

    public LinkService(LinkRepository linkRepository, TicketService ticketService) {
        this.linkRepository = linkRepository;
        this.ticketService = ticketService;
    }

    public Link saveLink(Link link) {
        return linkRepository.save(link);
    }

    public List<Link> getLinksByParentTicket(Long ticketId) {
        Ticket parentTicket = ticketService.getTicketById(ticketId)
                                .orElseThrow(() -> new LinkNotFoundException("Parent Ticket not found with ID: " + ticketId));
        return linkRepository.findByParentTicket(parentTicket);
    }

    public Optional<Link> getLinkById(Long id) {
        return linkRepository.findById(id);
    }

    public void deleteLink(Long id) {
        if (!linkRepository.existsById(id)) {
            throw new LinkNotFoundException("Link not found with ID: " + id);
        }
        linkRepository.deleteById(id);
    }
}