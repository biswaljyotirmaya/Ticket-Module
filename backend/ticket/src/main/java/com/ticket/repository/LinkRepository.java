package com.ticket.repository;

import com.ticket.entity.Link;
import com.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByParentTicket(Ticket parentTicket);
}