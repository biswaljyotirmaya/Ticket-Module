//package com.ticket.repository;
//
//import com.ticket.entity.Ticket;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@ActiveProfiles("test")
//class TicketRepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private TicketRepository ticketRepository;
//
//    private Ticket sampleTicket;
//
//    @BeforeEach
//    void setUp() {
//        sampleTicket = createSampleTicket();
//    }
//
//    @Test
//    void testSaveTicket() {
//        // When
//        Ticket savedTicket = ticketRepository.save(sampleTicket);
//
//        // Then
//        assertNotNull(savedTicket);
//        assertNotNull(savedTicket.getSno());
//        assertEquals("Test Ticket", savedTicket.getTitleTicket());
//        assertEquals("John Doe", savedTicket.getAssign());
//        assertEquals("Open", savedTicket.getStatus());
//        assertEquals("High", savedTicket.getPriority());
//        assertEquals(LocalDate.now(), savedTicket.getDateCurrent());
//        assertEquals("Test task", savedTicket.getSubTask());
//    }
//
//    @Test
//    void testFindById_Success() {
//        // Given
//        Ticket savedTicket = ticketRepository.save(sampleTicket);
//
//        // When
//        Optional<Ticket> foundTicket = ticketRepository.findById(savedTicket.getSno());
//
//        // Then
//        assertTrue(foundTicket.isPresent());
//        assertEquals(savedTicket.getSno(), foundTicket.get().getSno());
//        assertEquals("Test Ticket", foundTicket.get().getTitleTicket());
//    }
//
//    @Test
//    void testFindById_NotFound() {
//        // When
//        Optional<Ticket> foundTicket = ticketRepository.findById(999L);
//
//        // Then
//        assertFalse(foundTicket.isPresent());
//    }
//
//    @Test
//    void testFindAll_Success() {
//        // Given
//        Ticket ticket1 = createSampleTicket();
//        ticket1.setTitleTicket("Ticket 1");
//        ticketRepository.save(ticket1);
//
//        Ticket ticket2 = createSampleTicket();
//        ticket2.setTitleTicket("Ticket 2");
//        ticketRepository.save(ticket2);
//
//        // When
//        List<Ticket> allTickets = ticketRepository.findAll();
//
//        // Then
//        assertEquals(2, allTickets.size());
//        assertTrue(allTickets.stream().anyMatch(t -> "Ticket 1".equals(t.getTitleTicket())));
//        assertTrue(allTickets.stream().anyMatch(t -> "Ticket 2".equals(t.getTitleTicket())));
//    }
//
//    @Test
//    void testFindAll_Empty() {
//        // When
//        List<Ticket> allTickets = ticketRepository.findAll();
//
//        // Then
//        assertTrue(allTickets.isEmpty());
//    }
//
//    @Test
//    void testUpdateTicket() {
//        // Given
//        Ticket savedTicket = ticketRepository.save(sampleTicket);
//        savedTicket.setTitleTicket("Updated Ticket");
//        savedTicket.setStatus("In Progress");
//
//        // When
//        Ticket updatedTicket = ticketRepository.save(savedTicket);
//
//        // Then
//        assertEquals("Updated Ticket", updatedTicket.getTitleTicket());
//        assertEquals("In Progress", updatedTicket.getStatus());
//        assertEquals(savedTicket.getSno(), updatedTicket.getSno());
//    }
//
//    @Test
//    void testDeleteTicket() {
//        // Given
//        Ticket savedTicket = ticketRepository.save(sampleTicket);
//        Long ticketId = savedTicket.getSno();
//
//        // When
//        ticketRepository.deleteById(ticketId);
//
//        // Then
//        Optional<Ticket> deletedTicket = ticketRepository.findById(ticketId);
//        assertFalse(deletedTicket.isPresent());
//    }
//
//    @Test
//    void testExistsById_True() {
//        // Given
//        Ticket savedTicket = ticketRepository.save(sampleTicket);
//
//        // When
//        boolean exists = ticketRepository.existsById(savedTicket.getSno());
//
//        // Then
//        assertTrue(exists);
//    }
//
//    @Test
//    void testExistsById_False() {
//        // When
//        boolean exists = ticketRepository.existsById(999L);
//
//        // Then
//        assertFalse(exists);
//    }
//
//    @Test
//    void testSaveMultipleTickets() {
//        // Given
//        Ticket ticket1 = createSampleTicket();
//        ticket1.setTitleTicket("Ticket 1");
//        ticket1.setAssign("Alice");
//
//        Ticket ticket2 = createSampleTicket();
//        ticket2.setTitleTicket("Ticket 2");
//        ticket2.setAssign("Bob");
//
//        Ticket ticket3 = createSampleTicket();
//        ticket3.setTitleTicket("Ticket 3");
//        ticket3.setAssign("Charlie");
//
//        // When
//        List<Ticket> savedTickets = ticketRepository.saveAll(List.of(ticket1, ticket2, ticket3));
//
//        // Then
//        assertEquals(3, savedTickets.size());
//        assertTrue(savedTickets.stream().allMatch(t -> t.getSno() != null));
//        
//        List<Ticket> allTickets = ticketRepository.findAll();
//        assertEquals(3, allTickets.size());
//    }
//
//    @Test
//    void testTicketPersistence() {
//        // Given
//        Ticket ticket = createSampleTicket();
//        ticket.setSubTask("A very long sub task that might contain a lot of text and should be stored properly in the database");
//
//        // When
//        Ticket savedTicket = ticketRepository.save(ticket);
//
//        // Then
//        assertNotNull(savedTicket.getSno());
//        assertEquals("A very long sub task that might contain a lot of text and should be stored properly in the database", 
//                    savedTicket.getSubTask());
//    }
//
//    @Test
//    void testTestIsolation() {
//        // Given - This test should start with a clean database
//        long initialCount = ticketRepository.count();
//        assertEquals(0, initialCount, "Database should be clean at start of test");
//
//        // When
//        Ticket ticket = createSampleTicket();
//        ticketRepository.save(ticket);
//
//        // Then
//        assertEquals(1, ticketRepository.count(), "Should have exactly one ticket after save");
//
//        // Clean up
//        ticketRepository.deleteAll();
//        assertEquals(0, ticketRepository.count(), "Database should be clean after test");
//    }
//
//    private Ticket createSampleTicket() {
//        Ticket ticket = new Ticket();
//        ticket.setTitleTicket("Test Ticket");
//        ticket.setAssign("John Doe");
//        ticket.setStatus("Open");
//        ticket.setPriority("High");
//        ticket.setDateCurrent(LocalDate.now());
//        ticket.setSubTask("Test task");
//        return ticket;
//    }
//} 




package com.ticket.repository;

import com.ticket.entity.Ticket;
import org.junit.jupiter.api.AfterEach; // Added AfterEach for cleanup consistency
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat; // Added for more fluent assertions

@DataJpaTest
@ActiveProfiles("test")
class TicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // Keep TestEntityManager for direct interaction with persistence context

    @Autowired
    private TicketRepository ticketRepository;

    private Ticket sampleTicket;

    @BeforeEach
    void setUp() {
        // Clear repository before each test to ensure test isolation
        ticketRepository.deleteAll(); 
        sampleTicket = createSampleTicket();
    }

    @AfterEach // Added AfterEach for cleanup after each test
    void tearDown() {
        ticketRepository.deleteAll();
    }

    @Test
    void testSaveTicket() {
        // When
        Ticket savedTicket = ticketRepository.save(sampleTicket);
        entityManager.flush(); // Ensure changes are flushed to DB

        // Then
        assertNotNull(savedTicket);
        assertNotNull(savedTicket.getSno());
        assertEquals("Test Ticket", savedTicket.getTitleTicket());
        assertEquals("John Doe", savedTicket.getAssign());
        assertEquals("Open", savedTicket.getStatus());
        assertEquals("High", savedTicket.getPriority());
        assertEquals(LocalDate.now(), savedTicket.getDateCurrent());
        assertEquals("Test task description", savedTicket.getDescription()); // Changed getSubTask to getDescription
    }

    @Test
    void testFindById_Success() {
        // Given
        Ticket savedTicket = ticketRepository.save(sampleTicket);
        entityManager.flush();

        // When
        Optional<Ticket> foundTicket = ticketRepository.findById(savedTicket.getSno());

        // Then
        assertTrue(foundTicket.isPresent());
        assertEquals(savedTicket.getSno(), foundTicket.get().getSno());
        assertEquals("Test Ticket", foundTicket.get().getTitleTicket());
    }

    @Test
    void testFindById_NotFound() {
        // When
        Optional<Ticket> foundTicket = ticketRepository.findById(999L);

        // Then
        assertFalse(foundTicket.isPresent());
    }

    @Test
    void testFindAll_Success() {
        // Given
        Ticket ticket1 = createSampleTicket();
        ticket1.setTitleTicket("Ticket 1");
        ticketRepository.save(ticket1);

        Ticket ticket2 = createSampleTicket();
        ticket2.setTitleTicket("Ticket 2");
        ticketRepository.save(ticket2);
        entityManager.flush();

        // When
        List<Ticket> allTickets = ticketRepository.findAll();

        // Then
        assertEquals(2, allTickets.size());
        assertThat(allTickets.stream().anyMatch(t -> "Ticket 1".equals(t.getTitleTicket()))).isTrue();
        assertThat(allTickets.stream().anyMatch(t -> "Ticket 2".equals(t.getTitleTicket()))).isTrue();
    }

    @Test
    void testFindAll_Empty() {
        // When
        List<Ticket> allTickets = ticketRepository.findAll();

        // Then
        assertTrue(allTickets.isEmpty());
    }

    @Test
    void testUpdateTicket() {
        // Given
        Ticket savedTicket = ticketRepository.save(sampleTicket);
        entityManager.flush();
        
        savedTicket.setTitleTicket("Updated Ticket");
        savedTicket.setStatus("In Progress");
        savedTicket.setDescription("Updated task description"); // Changed setSubTask to setDescription

        // When
        Ticket updatedTicket = ticketRepository.save(savedTicket);
        entityManager.flush();

        // Then
        assertEquals("Updated Ticket", updatedTicket.getTitleTicket());
        assertEquals("In Progress", updatedTicket.getStatus());
        assertEquals(savedTicket.getSno(), updatedTicket.getSno());
        assertEquals("Updated task description", updatedTicket.getDescription()); // Changed getDescription
    }

    @Test
    void testDeleteTicket() {
        // Given
        Ticket savedTicket = ticketRepository.save(sampleTicket);
        Long ticketId = savedTicket.getSno();
        entityManager.flush();

        // When
        ticketRepository.deleteById(ticketId);
        entityManager.flush(); // Flush delete operation

        // Then
        Optional<Ticket> deletedTicket = ticketRepository.findById(ticketId);
        assertFalse(deletedTicket.isPresent());
    }

    @Test
    void testExistsById_True() {
        // Given
        Ticket savedTicket = ticketRepository.save(sampleTicket);
        entityManager.flush();

        // When
        boolean exists = ticketRepository.existsById(savedTicket.getSno());

        // Then
        assertTrue(exists);
    }

    @Test
    void testExistsById_False() {
        // When
        boolean exists = ticketRepository.existsById(999L);

        // Then
        assertFalse(exists);
    }

    @Test
    void testSaveMultipleTickets() {
        // Given
        Ticket ticket1 = createSampleTicket();
        ticket1.setTitleTicket("Ticket 1");
        ticket1.setAssign("Alice");

        Ticket ticket2 = createSampleTicket();
        ticket2.setTitleTicket("Ticket 2");
        ticket2.setAssign("Bob");

        Ticket ticket3 = createSampleTicket();
        ticket3.setTitleTicket("Ticket 3");
        ticket3.setAssign("Charlie");

        // When
        List<Ticket> savedTickets = ticketRepository.saveAll(List.of(ticket1, ticket2, ticket3));
        entityManager.flush();

        // Then
        assertEquals(3, savedTickets.size());
        assertTrue(savedTickets.stream().allMatch(t -> t.getSno() != null));
        
        List<Ticket> allTickets = ticketRepository.findAll();
        assertEquals(3, allTickets.size());
    }

    @Test
    void testTicketPersistenceLongDescription() { // Renamed for clarity
        // Given
        Ticket ticket = createSampleTicket();
        ticket.setDescription("A very long description that might contain a lot of text and should be stored properly in the database."); // Changed setSubTask to setDescription

        // When
        Ticket savedTicket = ticketRepository.save(ticket);
        entityManager.flush();

        // Then
        assertNotNull(savedTicket.getSno());
        assertEquals("A very long description that might contain a lot of text and should be stored properly in the database.",
                     savedTicket.getDescription()); // Changed getSubTask to getDescription

        // Verify it can be retrieved correctly
        Optional<Ticket> retrieved = ticketRepository.findById(savedTicket.getSno());
        assertTrue(retrieved.isPresent());
        assertEquals("A very long description that might contain a lot of text and should be stored properly in the database.", retrieved.get().getDescription());
    }

    @Test
    void testTestIsolation() {
        // Given - This test should start with a clean database as DataJpaTest rolls back transactions
        long initialCount = ticketRepository.count();
        assertEquals(0, initialCount, "Database should be clean at start of test");

        // When
        Ticket ticket = createSampleTicket();
        ticketRepository.save(ticket);
        entityManager.flush(); // Ensure it's saved before counting

        // Then
        assertEquals(1, ticketRepository.count(), "Should have exactly one ticket after save");
        // No explicit deleteAll needed here as DataJpaTest will roll back changes.
    }

    // --- Helper method for creating sample Ticket entity ---
    private Ticket createSampleTicket() {
        Ticket ticket = new Ticket();
        // ID is not set here, let JPA handle it
        ticket.setTitleTicket("Test Ticket");
        ticket.setAssign("John Doe");
        ticket.setStatus("Open");
        ticket.setPriority("High");
        ticket.setDateCurrent(LocalDate.now());
        ticket.setDescription("Test task description"); // Changed setSubTask to setDescription
        return ticket;
    }
}