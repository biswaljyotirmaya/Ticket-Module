package com.ticket.repository;

import com.ticket.entity.Relationship;
import com.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    List<Relationship> findBySourceTicket(Ticket sourceTicket);
}