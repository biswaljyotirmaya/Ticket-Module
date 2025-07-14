//package com.ticket.repository;
//
//import com.ticket.entity.SubTicket;
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
//class SubTicketRepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private SubTicketRepository subTicketRepository;
//
//    @Autowired
//    private com.ticket.repository.TicketRepository ticketRepository;
//
//    private SubTicket sampleSubTicket;
//    private Ticket sampleParentTicket;
//
//    @BeforeEach
//    void setUp() {
//        sampleParentTicket = createSampleTicket();
//        sampleSubTicket = createSampleSubTicket();
//        sampleSubTicket.setParentTicket(sampleParentTicket);
//    }
//
//    @Test
//    void testSaveSubTicket() {
//        // Given
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//
//        // When
//        SubTicket savedSubTicket = subTicketRepository.save(sampleSubTicket);
//
//        // Then
//        assertNotNull(savedSubTicket);
//        assertNotNull(savedSubTicket.getId());
//        assertEquals("Test SubTicket", savedSubTicket.getTitle());
//        assertEquals("Jane Smith", savedSubTicket.getAssign());
//        assertEquals("In Progress", savedSubTicket.getStatus());
//        assertEquals("Medium", savedSubTicket.getPriority());
//        assertEquals(LocalDate.now(), savedSubTicket.getDateCurrent());
//        assertEquals("Test subtask", savedSubTicket.getSubTask());
//        assertEquals(savedParentTicket.getSno(), savedSubTicket.getParentTicket().getSno());
//    }
//
//    @Test
//    void testFindById_Success() {
//        // Given
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//        SubTicket savedSubTicket = subTicketRepository.save(sampleSubTicket);
//
//        // When
//        Optional<SubTicket> foundSubTicket = subTicketRepository.findById(savedSubTicket.getId());
//
//        // Then
//        assertTrue(foundSubTicket.isPresent());
//        assertEquals(savedSubTicket.getId(), foundSubTicket.get().getId());
//        assertEquals("Test SubTicket", foundSubTicket.get().getTitle());
//        assertEquals(savedParentTicket.getSno(), foundSubTicket.get().getParentTicket().getSno());
//    }
//
//    @Test
//    void testFindById_NotFound() {
//        // When
//        Optional<SubTicket> foundSubTicket = subTicketRepository.findById(999L);
//
//        // Then
//        assertFalse(foundSubTicket.isPresent());
//    }
//
//    @Test
//    void testFindAll() {
//        // Given
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//        
//        SubTicket subTicket1 = createSampleSubTicket();
//        subTicket1.setTitle("SubTicket 1");
//        subTicket1.setParentTicket(savedParentTicket);
//        subTicketRepository.save(subTicket1);
//
//        SubTicket subTicket2 = createSampleSubTicket();
//        subTicket2.setTitle("SubTicket 2");
//        subTicket2.setParentTicket(savedParentTicket);
//        subTicketRepository.save(subTicket2);
//
//        // When
//        List<SubTicket> allSubTickets = subTicketRepository.findAll();
//
//        // Then
//        assertEquals(2, allSubTickets.size());
//        assertTrue(allSubTickets.stream().anyMatch(st -> st.getTitle().equals("SubTicket 1")));
//        assertTrue(allSubTickets.stream().anyMatch(st -> st.getTitle().equals("SubTicket 2")));
//    }
//
//    @Test
//    void testFindByParentTicket() {
//        // Given
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//        
//        SubTicket subTicket1 = createSampleSubTicket();
//        subTicket1.setTitle("SubTicket 1");
//        subTicket1.setParentTicket(savedParentTicket);
//        subTicketRepository.save(subTicket1);
//
//        SubTicket subTicket2 = createSampleSubTicket();
//        subTicket2.setTitle("SubTicket 2");
//        subTicket2.setParentTicket(savedParentTicket);
//        subTicketRepository.save(subTicket2);
//
//        // When
//        List<SubTicket> subTicketsForParent = subTicketRepository.findByParentTicket(savedParentTicket);
//
//        // Then
//        assertEquals(2, subTicketsForParent.size());
//        assertTrue(subTicketsForParent.stream().allMatch(st -> st.getParentTicket().getSno().equals(savedParentTicket.getSno())));
//    }
//
//    @Test
//    void testFindByParentTicket_EmptyList() {
//        // Given
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//
//        // When
//        List<SubTicket> subTicketsForParent = subTicketRepository.findByParentTicket(savedParentTicket);
//
//        // Then
//        assertTrue(subTicketsForParent.isEmpty());
//    }
//
//    @Test
//    void testDeleteSubTicket() {
//        // Given
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//        SubTicket savedSubTicket = subTicketRepository.save(sampleSubTicket);
//
//        // When
//        subTicketRepository.deleteById(savedSubTicket.getId());
//
//        // Then
//        Optional<SubTicket> foundSubTicket = subTicketRepository.findById(savedSubTicket.getId());
//        assertFalse(foundSubTicket.isPresent());
//    }
//
//    @Test
//    void testExistsById_True() {
//        // Given
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//        SubTicket savedSubTicket = subTicketRepository.save(sampleSubTicket);
//
//        // When
//        boolean exists = subTicketRepository.existsById(savedSubTicket.getId());
//
//        // Then
//        assertTrue(exists);
//    }
//
//    @Test
//    void testExistsById_False() {
//        // When
//        boolean exists = subTicketRepository.existsById(999L);
//
//        // Then
//        assertFalse(exists);
//    }
//
//    @Test
//    void testSaveMultipleSubTickets() {
//        // Given
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//        
//        SubTicket subTicket1 = createSampleSubTicket();
//        subTicket1.setTitle("SubTicket 1");
//        subTicket1.setParentTicket(savedParentTicket);
//
//        SubTicket subTicket2 = createSampleSubTicket();
//        subTicket2.setTitle("SubTicket 2");
//        subTicket2.setParentTicket(savedParentTicket);
//
//        SubTicket subTicket3 = createSampleSubTicket();
//        subTicket3.setTitle("SubTicket 3");
//        subTicket3.setParentTicket(savedParentTicket);
//
//        // When
//        List<SubTicket> savedSubTickets = subTicketRepository.saveAll(List.of(subTicket1, subTicket2, subTicket3));
//
//        // Then
//        assertEquals(3, savedSubTickets.size());
//        assertTrue(savedSubTickets.stream().allMatch(st -> st.getId() != null));
//        
//        List<SubTicket> allSubTickets = subTicketRepository.findAll();
//        assertEquals(3, allSubTickets.size());
//    }
//
//    @Test
//    void testSubTicketPersistence() {
//        // Given
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//        SubTicket subTicket = createSampleSubTicket();
//        subTicket.setSubTask("A very long subtask that might contain a lot of text and should be stored properly in the database");
//        subTicket.setParentTicket(savedParentTicket);
//
//        // When
//        SubTicket savedSubTicket = subTicketRepository.save(subTicket);
//
//        // Then
//        assertNotNull(savedSubTicket.getId());
//        assertEquals("A very long subtask that might contain a lot of text and should be stored properly in the database", 
//                    savedSubTicket.getSubTask());
//    }
//
//    @Test
//    void testTestIsolation() {
//        // Given - This test should start with a clean database
//        long initialCount = subTicketRepository.count();
//        assertEquals(0, initialCount, "Database should be clean at start of test");
//
//        // When
//        Ticket savedParentTicket = ticketRepository.save(sampleParentTicket);
//        SubTicket subTicket = createSampleSubTicket();
//        subTicket.setParentTicket(savedParentTicket);
//        subTicketRepository.save(subTicket);
//
//        // Then
//        assertEquals(1, subTicketRepository.count(), "Should have exactly one subticket after save");
//
//        // Clean up
//        subTicketRepository.deleteAll();
//        assertEquals(0, subTicketRepository.count(), "Database should be clean after test");
//    }
//
//    @Test
//    void testSubTicketWithDifferentParents() {
//        // Given
//        Ticket parent1 = createSampleTicket();
//        parent1.setTitleTicket("Parent 1");
//        Ticket savedParent1 = ticketRepository.save(parent1);
//
//        Ticket parent2 = createSampleTicket();
//        parent2.setTitleTicket("Parent 2");
//        Ticket savedParent2 = ticketRepository.save(parent2);
//
//        SubTicket subTicket1 = createSampleSubTicket();
//        subTicket1.setTitle("SubTicket for Parent 1");
//        subTicket1.setParentTicket(savedParent1);
//        subTicketRepository.save(subTicket1);
//
//        SubTicket subTicket2 = createSampleSubTicket();
//        subTicket2.setTitle("SubTicket for Parent 2");
//        subTicket2.setParentTicket(savedParent2);
//        subTicketRepository.save(subTicket2);
//
//        // When
//        List<SubTicket> subTicketsForParent1 = subTicketRepository.findByParentTicket(savedParent1);
//        List<SubTicket> subTicketsForParent2 = subTicketRepository.findByParentTicket(savedParent2);
//
//        // Then
//        assertEquals(1, subTicketsForParent1.size());
//        assertEquals("SubTicket for Parent 1", subTicketsForParent1.get(0).getTitle());
//        
//        assertEquals(1, subTicketsForParent2.size());
//        assertEquals("SubTicket for Parent 2", subTicketsForParent2.get(0).getTitle());
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
//
//    private SubTicket createSampleSubTicket() {
//        SubTicket subTicket = new SubTicket();
//        subTicket.setTitle("Test SubTicket");
//        subTicket.setAssign("Jane Smith");
//        subTicket.setStatus("In Progress");
//        subTicket.setPriority("Medium");
//        subTicket.setDateCurrent(LocalDate.now());
//        subTicket.setSubTask("Test subtask");
//        return subTicket;
//    }
//} 



package com.ticket.repository;

import com.ticket.entity.SubTicket;
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
class SubTicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // Keep TestEntityManager for direct interaction with persistence context

    @Autowired
    private SubTicketRepository subTicketRepository;

    @Autowired
    private com.ticket.repository.TicketRepository ticketRepository;

    private SubTicket sampleSubTicket;
    private Ticket sampleParentTicket;

    @BeforeEach
    void setUp() {
        // Clear repositories before each test to ensure test isolation
        subTicketRepository.deleteAll();
        ticketRepository.deleteAll();

        sampleParentTicket = createSampleTicket();
        // Persist parent ticket first, as it's a ManyToOne relationship
        entityManager.persistAndFlush(sampleParentTicket); // Use entityManager to ensure it's persisted correctly

        sampleSubTicket = createSampleSubTicket();
        sampleSubTicket.setParentTicket(sampleParentTicket); // Link to the persisted parent
    }

    @AfterEach // Added AfterEach for cleanup after each test
    void tearDown() {
        subTicketRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    void testSaveSubTicket() {
        // Given - parentTicket is already persisted in setUp

        // When
        SubTicket savedSubTicket = subTicketRepository.save(sampleSubTicket);
        entityManager.flush(); // Ensure changes are flushed to DB

        // Then
        assertNotNull(savedSubTicket);
        assertNotNull(savedSubTicket.getId());
        assertEquals("Test SubTicket", savedSubTicket.getTitle());
        assertEquals("Jane Smith", savedSubTicket.getAssign());
        assertEquals("In Progress", savedSubTicket.getStatus());
        assertEquals("Medium", savedSubTicket.getPriority());
        assertEquals(LocalDate.now(), savedSubTicket.getDateCurrent());
        assertEquals("Test subtask description", savedSubTicket.getDescription()); // Changed getSubTask to getDescription
        assertEquals(sampleParentTicket.getSno(), savedSubTicket.getParentTicket().getSno());
    }

    @Test
    void testFindById_Success() {
        // Given
        SubTicket savedSubTicket = subTicketRepository.save(sampleSubTicket);
        entityManager.flush();

        // When
        Optional<SubTicket> foundSubTicket = subTicketRepository.findById(savedSubTicket.getId());

        // Then
        assertTrue(foundSubTicket.isPresent());
        assertEquals(savedSubTicket.getId(), foundSubTicket.get().getId());
        assertEquals("Test SubTicket", foundSubTicket.get().getTitle());
        assertEquals(sampleParentTicket.getSno(), foundSubTicket.get().getParentTicket().getSno());
    }

    @Test
    void testFindById_NotFound() {
        // When
        Optional<SubTicket> foundSubTicket = subTicketRepository.findById(999L);

        // Then
        assertFalse(foundSubTicket.isPresent());
    }

    @Test
    void testFindAll() {
        // Given - sampleSubTicket is already configured and linked to sampleParentTicket in setUp
        subTicketRepository.save(sampleSubTicket); // Save the first one
        
        SubTicket subTicket1 = createSampleSubTicket();
        subTicket1.setTitle("SubTicket 1");
        subTicket1.setParentTicket(sampleParentTicket); // Link to the same parent
        subTicketRepository.save(subTicket1);

        SubTicket subTicket2 = createSampleSubTicket();
        subTicket2.setTitle("SubTicket 2");
        subTicket2.setParentTicket(sampleParentTicket); // Link to the same parent
        subTicketRepository.save(subTicket2);
        entityManager.flush();

        // When
        List<SubTicket> allSubTickets = subTicketRepository.findAll();

        // Then
        assertEquals(3, allSubTickets.size()); // Expect 3: sampleSubTicket, subTicket1, subTicket2
        assertThat(allSubTickets).extracting(SubTicket::getTitle)
                                 .containsExactlyInAnyOrder("Test SubTicket", "SubTicket 1", "SubTicket 2");
    }

    @Test
    void testFindByParentTicket() {
        // Given - sampleSubTicket already linked and ready to be saved
        subTicketRepository.save(sampleSubTicket); // Save the first one
        
        SubTicket subTicket1 = createSampleSubTicket();
        subTicket1.setTitle("SubTicket 1");
        subTicket1.setParentTicket(sampleParentTicket);
        subTicketRepository.save(subTicket1);

        SubTicket subTicket2 = createSampleSubTicket();
        subTicket2.setTitle("SubTicket 2");
        subTicket2.setParentTicket(sampleParentTicket);
        subTicketRepository.save(subTicket2);
        entityManager.flush();

        // When
        List<SubTicket> subTicketsForParent = subTicketRepository.findByParentTicket(sampleParentTicket);

        // Then
        assertEquals(3, subTicketsForParent.size()); // Expect 3
        assertThat(subTicketsForParent.stream().allMatch(st -> st.getParentTicket().getSno().equals(sampleParentTicket.getSno()))).isTrue();
    }

    @Test
    void testFindByParentTicket_EmptyList() {
        // Given - sampleParentTicket is saved, but no subtickets are linked to it in this test
        // sampleSubTicket is NOT saved in this specific test method.

        // When
        List<SubTicket> subTicketsForParent = subTicketRepository.findByParentTicket(sampleParentTicket);

        // Then
        assertTrue(subTicketsForParent.isEmpty());
    }

    @Test
    void testDeleteSubTicket() {
        // Given
        SubTicket savedSubTicket = subTicketRepository.save(sampleSubTicket);
        entityManager.flush();

        // When
        subTicketRepository.deleteById(savedSubTicket.getId());
        entityManager.flush(); // Flush delete operation

        // Then
        Optional<SubTicket> foundSubTicket = subTicketRepository.findById(savedSubTicket.getId());
        assertFalse(foundSubTicket.isPresent());
    }

    @Test
    void testExistsById_True() {
        // Given
        SubTicket savedSubTicket = subTicketRepository.save(sampleSubTicket);
        entityManager.flush();

        // When
        boolean exists = subTicketRepository.existsById(savedSubTicket.getId());

        // Then
        assertTrue(exists);
    }

    @Test
    void testExistsById_False() {
        // When
        boolean exists = subTicketRepository.existsById(999L);

        // Then
        assertFalse(exists);
    }

    @Test
    void testSaveMultipleSubTickets() {
        // Given
        SubTicket subTicket1 = createSampleSubTicket();
        subTicket1.setTitle("SubTicket 1");
        subTicket1.setParentTicket(sampleParentTicket);

        SubTicket subTicket2 = createSampleSubTicket();
        subTicket2.setTitle("SubTicket 2");
        subTicket2.setParentTicket(sampleParentTicket);

        SubTicket subTicket3 = createSampleSubTicket();
        subTicket3.setTitle("SubTicket 3");
        subTicket3.setParentTicket(sampleParentTicket);

        // When
        List<SubTicket> savedSubTickets = subTicketRepository.saveAll(List.of(subTicket1, subTicket2, subTicket3));
        entityManager.flush();

        // Then
        assertEquals(3, savedSubTickets.size());
        assertTrue(savedSubTickets.stream().allMatch(st -> st.getId() != null));
        
        List<SubTicket> allSubTickets = subTicketRepository.findAll();
        assertEquals(3, allSubTickets.size());
    }

    @Test
    void testSubTicketPersistenceLongDescription() { // Renamed for clarity
        // Given
        SubTicket subTicket = createSampleSubTicket();
        subTicket.setDescription("A very long subtask that might contain a lot of text and should be stored properly in the database."); // Changed setSubTask to setDescription
        subTicket.setParentTicket(sampleParentTicket);

        // When
        SubTicket savedSubTicket = subTicketRepository.save(subTicket);
        entityManager.flush(); // Ensure it's persisted

        // Then
        assertNotNull(savedSubTicket.getId());
        assertEquals("A very long subtask that might contain a lot of text and should be stored properly in the database.", 
                     savedSubTicket.getDescription()); // Changed getSubTask to getDescription

        // Verify it can be retrieved correctly
        Optional<SubTicket> retrieved = subTicketRepository.findById(savedSubTicket.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("A very long subtask that might contain a lot of text and should be stored properly in the database.", retrieved.get().getDescription());
    }

    @Test
    void testTestIsolation() {
        // Given - This test should start with a clean database as DataJpaTest rolls back transactions
        long initialCount = subTicketRepository.count();
        assertEquals(0, initialCount, "Database should be clean at start of test");

        // When
        SubTicket subTicket = createSampleSubTicket();
        subTicket.setParentTicket(sampleParentTicket); // Use the already persisted parent
        subTicketRepository.save(subTicket);
        entityManager.flush(); // Ensure it's saved before counting

        // Then
        assertEquals(1, subTicketRepository.count(), "Should have exactly one subticket after save");
        // No explicit deleteAll needed here as DataJpaTest will roll back changes.
    }

    @Test
    void testSubTicketWithDifferentParents() {
        // Given
        Ticket parent1 = createSampleTicket();
        parent1.setTitleTicket("Parent 1");
        entityManager.persistAndFlush(parent1); // Persist through entity manager

        Ticket parent2 = createSampleTicket();
        parent2.setTitleTicket("Parent 2");
        entityManager.persistAndFlush(parent2); // Persist through entity manager

        SubTicket subTicket1 = createSampleSubTicket();
        subTicket1.setTitle("SubTicket for Parent 1");
        subTicket1.setParentTicket(parent1);
        subTicketRepository.save(subTicket1);

        SubTicket subTicket2 = createSampleSubTicket();
        subTicket2.setTitle("SubTicket for Parent 2");
        subTicket2.setParentTicket(parent2);
        subTicketRepository.save(subTicket2);
        entityManager.flush();

        // When
        List<SubTicket> subTicketsForParent1 = subTicketRepository.findByParentTicket(parent1);
        List<SubTicket> subTicketsForParent2 = subTicketRepository.findByParentTicket(parent2);

        // Then
        assertEquals(1, subTicketsForParent1.size());
        assertEquals("SubTicket for Parent 1", subTicketsForParent1.get(0).getTitle());
        
        assertEquals(1, subTicketsForParent2.size());
        assertEquals("SubTicket for Parent 2", subTicketsForParent2.get(0).getTitle());
    }

    // --- Helper methods for creating sample entities ---
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

    private SubTicket createSampleSubTicket() {
        SubTicket subTicket = new SubTicket();
        // ID is not set here, let JPA handle it
        subTicket.setTitle("Test SubTicket");
        subTicket.setAssign("Jane Smith");
        subTicket.setStatus("In Progress");
        subTicket.setPriority("Medium");
        subTicket.setDateCurrent(LocalDate.now());
        subTicket.setDescription("Test subtask description"); // Changed setSubTask to setDescription
        return subTicket;
    }
}