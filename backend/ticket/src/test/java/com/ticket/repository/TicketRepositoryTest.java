package com.ticket.repository;

import com.ticket.entity.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    private Ticket sampleTicket;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll(); 
        sampleTicket = createSampleTicket();
    }

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
    }

    // ---------- Save, Update, Delete Tests ----------

    @Test
    void testSaveTicket() {
        Ticket saved = ticketRepository.save(sampleTicket);
        entityManager.flush();

        assertNotNull(saved.getId());
        assertEquals("Test Ticket", saved.getTitleTicket());
        assertEquals("Test task description", saved.getDescription());
    }

    @Test
    void testUpdateTicket() {
        Ticket saved = ticketRepository.save(sampleTicket);
        saved.setTitleTicket("Updated Ticket");
        saved.setStatus("In Progress");
        saved.setDescription("Updated description");

        Ticket updated = ticketRepository.save(saved);
        entityManager.flush();

        assertEquals("Updated Ticket", updated.getTitleTicket());
        assertEquals("In Progress", updated.getStatus());
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    void testDeleteTicket() {
        Ticket saved = ticketRepository.save(sampleTicket);
        Long id = saved.getId();
        ticketRepository.deleteById(id);
        entityManager.flush();

        assertFalse(ticketRepository.findById(id).isPresent());
    }

    // ---------- Find By ID ----------

    @Test
    void testFindById_Success() {
        Ticket saved = ticketRepository.save(sampleTicket);
        entityManager.flush();

        Optional<Ticket> found = ticketRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Ticket> found = ticketRepository.findById(999L);
        assertFalse(found.isPresent());
    }

    // ---------- Find All ----------

    @Test
    void testFindAll_WhenDataExists() {
        Ticket t1 = createSampleTicket(); t1.setTitleTicket("Ticket 1");
        Ticket t2 = createSampleTicket(); t2.setTitleTicket("Ticket 2");

        ticketRepository.saveAll(List.of(t1, t2));
        entityManager.flush();

        List<Ticket> result = ticketRepository.findAll();

        assertEquals(2, result.size());
        assertThat(result).extracting(Ticket::getTitleTicket)
                          .containsExactlyInAnyOrder("Ticket 1", "Ticket 2");
    }

    @Test
    void testFindAll_WhenEmpty() {
        List<Ticket> result = ticketRepository.findAll();
        assertTrue(result.isEmpty());
    }

    // ---------- Existence Check ----------

    @Test
    void testExistsById_True() {
        Ticket saved = ticketRepository.save(sampleTicket);
        assertTrue(ticketRepository.existsById(saved.getId()));
    }

    @Test
    void testExistsById_False() {
        assertFalse(ticketRepository.existsById(123L));
    }

    // ---------- Save Multiple ----------

    @Test
    void testSaveMultipleTickets() {
        Ticket t1 = createSampleTicket(); t1.setTitleTicket("Ticket 1");
        Ticket t2 = createSampleTicket(); t2.setTitleTicket("Ticket 2");
        Ticket t3 = createSampleTicket(); t3.setTitleTicket("Ticket 3");

        List<Ticket> saved = ticketRepository.saveAll(List.of(t1, t2, t3));
        entityManager.flush();

        assertEquals(3, saved.size());
        assertThat(saved).allMatch(t -> t.getId() != null);
    }

    // ---------- Custom Data Validations ----------

    @Test
    void testTicketPersistence_WithLongDescription() {
        String longDesc = "This is a very long description meant to test DB storage limits.";
        sampleTicket.setDescription(longDesc);

        Ticket saved = ticketRepository.save(sampleTicket);
        entityManager.flush();

        Optional<Ticket> retrieved = ticketRepository.findById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(longDesc, retrieved.get().getDescription());
    }

    @Test
    void testDatabaseIsIsolatedPerTest() {
        assertEquals(0, ticketRepository.count());

        ticketRepository.save(createSampleTicket());
        entityManager.flush();

        assertEquals(1, ticketRepository.count());
    }

    // ---------- Helper ----------

    private Ticket createSampleTicket() {
        Ticket ticket = new Ticket();
        ticket.setTitleTicket("Test Ticket");
        ticket.setAssign("John Doe");
        ticket.setStatus("Open");
        ticket.setPriority("High");
        ticket.setDateCurrent(LocalDate.now());
        ticket.setDescription("Test task description");
        return ticket;
    }
}
