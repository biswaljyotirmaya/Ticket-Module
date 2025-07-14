//package com.ticket.service;
//
//import com.ticket.entity.Ticket;
//import com.ticket.exception.TicketNotFoundException;
//import com.ticket.repository.TicketRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TicketServiceTest {
//
//    @Mock
//    private TicketRepository ticketRepository;
//
//    @InjectMocks
//    private TicketService ticketService;
//
//    private Ticket sampleTicket;
//
//    @BeforeEach
//    void setUp() {
//        sampleTicket = createSampleTicket();
//    }
//
//    @Test
//    void testSaveTicket_Success() {
//        // Given
//        when(ticketRepository.save(any(Ticket.class))).thenReturn(sampleTicket);
//
//        // When
//        Ticket savedTicket = ticketService.saveTicket(sampleTicket);
//
//        // Then
//        assertNotNull(savedTicket);
//        assertEquals("Test Ticket", savedTicket.getTitleTicket());
//        assertEquals("John Doe", savedTicket.getAssign());
//        verify(ticketRepository, times(1)).save(sampleTicket);
//    }
//
//    @Test
//    void testGetAllTickets_Success() {
//        // Given
//        List<Ticket> expectedTickets = Arrays.asList(sampleTicket, createSampleTicket());
//        when(ticketRepository.findAll()).thenReturn(expectedTickets);
//
//        // When
//        List<Ticket> actualTickets = ticketService.getAllTickets();
//
//        // Then
//        assertNotNull(actualTickets);
//        assertEquals(2, actualTickets.size());
//        verify(ticketRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetAllTickets_EmptyList() {
//        // Given
//        when(ticketRepository.findAll()).thenReturn(Arrays.asList());
//
//        // When
//        List<Ticket> actualTickets = ticketService.getAllTickets();
//
//        // Then
//        assertNotNull(actualTickets);
//        assertTrue(actualTickets.isEmpty());
//        verify(ticketRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetTicketById_Success() {
//        // Given
//        Long ticketId = 1L;
//        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(sampleTicket));
//
//        // When
//        Optional<Ticket> actualTicket = ticketService.getTicketById(ticketId);
//
//        // Then
//        assertTrue(actualTicket.isPresent());
//        assertEquals("Test Ticket", actualTicket.get().getTitleTicket());
//        verify(ticketRepository, times(1)).findById(ticketId);
//    }
//
//    @Test
//    void testGetTicketById_NotFound() {
//        // Given
//        Long ticketId = 999L;
//        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
//
//        // When
//        Optional<Ticket> actualTicket = ticketService.getTicketById(ticketId);
//
//        // Then
//        assertFalse(actualTicket.isPresent());
//        verify(ticketRepository, times(1)).findById(ticketId);
//    }
//
//    @Test
//    void testUpdateTicket_Success() {
//        // Given
//        Long ticketId = 1L;
//        Ticket existingTicket = createSampleTicket();
//        existingTicket.setSno(ticketId);
//        
//        Ticket updatedTicket = createSampleTicket();
//        updatedTicket.setTitleTicket("Updated Ticket");
//        updatedTicket.setStatus("In Progress");
//        
//        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
//        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);
//
//        // When
//        Ticket result = ticketService.updateTicket(ticketId, updatedTicket);
//
//        // Then
//        assertNotNull(result);
//        assertEquals("Updated Ticket", result.getTitleTicket());
//        assertEquals("In Progress", result.getStatus());
//        verify(ticketRepository, times(1)).findById(ticketId);
//        verify(ticketRepository, times(1)).save(any(Ticket.class));
//    }
//
//    @Test
//    void testUpdateTicket_NotFound() {
//        // Given
//        Long ticketId = 999L;
//        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(TicketNotFoundException.class, () -> {
//            ticketService.updateTicket(ticketId, sampleTicket);
//        });
//        
//        verify(ticketRepository, times(1)).findById(ticketId);
//        verify(ticketRepository, never()).save(any(Ticket.class));
//    }
//
//    @Test
//    void testDeleteTicket_Success() {
//        // Given
//        Long ticketId = 1L;
//        when(ticketRepository.existsById(ticketId)).thenReturn(true);
//        doNothing().when(ticketRepository).deleteById(ticketId);
//
//        // When
//        ticketService.deleteTicket(ticketId);
//
//        // Then
//        verify(ticketRepository, times(1)).existsById(ticketId);
//        verify(ticketRepository, times(1)).deleteById(ticketId);
//    }
//
//    @Test
//    void testDeleteTicket_NotFound() {
//        // Given
//        Long ticketId = 999L;
//        when(ticketRepository.existsById(ticketId)).thenReturn(false);
//
//        // When & Then
//        assertThrows(TicketNotFoundException.class, () -> {
//            ticketService.deleteTicket(ticketId);
//        });
//        
//        verify(ticketRepository, times(1)).existsById(ticketId);
//        verify(ticketRepository, never()).deleteById(any());
//    }
//
//    @Test
//    void testUpdateTicket_VerifyAllFieldsUpdated() {
//        // Given
//        Long ticketId = 1L;
//        Ticket existingTicket = createSampleTicket();
//        existingTicket.setSno(ticketId);
//        
//        Ticket updatedTicket = createSampleTicket();
//        updatedTicket.setTitleTicket("New Title");
//        updatedTicket.setAssign("Jane Smith");
//        updatedTicket.setStatus("Completed");
//        updatedTicket.setPriority("Low");
//        updatedTicket.setDateCurrent(LocalDate.of(2024, 1, 20));
//        updatedTicket.setSubTask("New sub task");
//        
//        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
//        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);
//
//        // When
//        Ticket result = ticketService.updateTicket(ticketId, updatedTicket);
//
//        // Then
//        assertNotNull(result);
//        assertEquals("New Title", result.getTitleTicket());
//        assertEquals("Jane Smith", result.getAssign());
//        assertEquals("Completed", result.getStatus());
//        assertEquals("Low", result.getPriority());
//        assertEquals(LocalDate.of(2024, 1, 20), result.getDateCurrent());
//        assertEquals("New sub task", result.getSubTask());
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


package com.ticket.service;

import com.ticket.entity.Ticket;
import com.ticket.exception.TicketNotFoundException;
import com.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket sampleTicket;

    @BeforeEach
    void setUp() {
        sampleTicket = createSampleTicket();
    }

    @Test
    void testSaveTicket_Success() {
        // Given
        when(ticketRepository.save(any(Ticket.class))).thenReturn(sampleTicket);

        // When
        Ticket savedTicket = ticketService.saveTicket(sampleTicket);

        // Then
        assertNotNull(savedTicket);
        assertEquals("Test Ticket", savedTicket.getTitleTicket());
        assertEquals("John Doe", savedTicket.getAssign());
        verify(ticketRepository, times(1)).save(sampleTicket);
    }

    @Test
    void testGetAllTickets_Success() {
        // Given
        List<Ticket> expectedTickets = Arrays.asList(sampleTicket, createSampleTicket());
        when(ticketRepository.findAll()).thenReturn(expectedTickets);

        // When
        List<Ticket> actualTickets = ticketService.getAllTickets();

        // Then
        assertNotNull(actualTickets);
        assertEquals(2, actualTickets.size());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTickets_EmptyList() {
        // Given
        when(ticketRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Ticket> actualTickets = ticketService.getAllTickets();

        // Then
        assertNotNull(actualTickets);
        assertTrue(actualTickets.isEmpty());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testGetTicketById_Success() {
        // Given
        Long ticketId = 1L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(sampleTicket));

        // When
        Optional<Ticket> actualTicket = ticketService.getTicketById(ticketId);

        // Then
        assertTrue(actualTicket.isPresent());
        assertEquals("Test Ticket", actualTicket.get().getTitleTicket());
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void testGetTicketById_NotFound() {
        // Given
        Long ticketId = 999L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // When & Then
        assertFalse(ticketService.getTicketById(ticketId).isPresent());
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void testUpdateTicket_Success() {
        // Given
        Long ticketId = 1L;
        Ticket existingTicket = createSampleTicket();
        existingTicket.setSno(ticketId);
        
        Ticket updatedTicket = createSampleTicket();
        updatedTicket.setTitleTicket("Updated Ticket");
        updatedTicket.setStatus("In Progress");
        
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);

        // When
        Ticket result = ticketService.updateTicket(ticketId, updatedTicket);

        // Then
        assertNotNull(result);
        assertEquals("Updated Ticket", result.getTitleTicket());
        assertEquals("In Progress", result.getStatus());
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testUpdateTicket_NotFound() {
        // Given
        Long ticketId = 999L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.updateTicket(ticketId, sampleTicket);
        });
        
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void testDeleteTicket_Success() {
        // Given
        Long ticketId = 1L;
        when(ticketRepository.existsById(ticketId)).thenReturn(true);
        doNothing().when(ticketRepository).deleteById(ticketId);

        // When
        ticketService.deleteTicket(ticketId);

        // Then
        verify(ticketRepository, times(1)).existsById(ticketId);
        verify(ticketRepository, times(1)).deleteById(ticketId);
    }

    @Test
    void testDeleteTicket_NotFound() {
        // Given
        Long ticketId = 999L;
        when(ticketRepository.existsById(ticketId)).thenReturn(false);

        // When & Then
        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.deleteTicket(ticketId);
        });
        
        verify(ticketRepository, times(1)).existsById(ticketId);
        verify(ticketRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateTicket_VerifyAllFieldsUpdated() {
        // Given
        Long ticketId = 1L;
        Ticket existingTicket = createSampleTicket();
        existingTicket.setSno(ticketId);
        
        Ticket updatedTicket = createSampleTicket();
        updatedTicket.setTitleTicket("New Title");
        updatedTicket.setAssign("Jane Smith");
        updatedTicket.setStatus("Completed");
        updatedTicket.setPriority("Low");
        updatedTicket.setDateCurrent(LocalDate.of(2024, 1, 20));
        updatedTicket.setDescription("New sub task description"); // Changed setSubTask to setDescription
        
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);

        // When
        Ticket result = ticketService.updateTicket(ticketId, updatedTicket);

        // Then
        assertNotNull(result);
        assertEquals("New Title", result.getTitleTicket());
        assertEquals("Jane Smith", result.getAssign());
        assertEquals("Completed", result.getStatus());
        assertEquals("Low", result.getPriority());
        assertEquals(LocalDate.of(2024, 1, 20), result.getDateCurrent());
        assertEquals("New sub task description", result.getDescription()); // Changed getSubTask to getDescription
    }

    @Test
    void testUpdateTicketDescription_Success() {
        Long ticketId = 1L;
        String newDescription = "Updated ticket description for testing.";
        Ticket existingTicket = createSampleTicket();
        existingTicket.setSno(ticketId);
        
        Ticket updatedTicket = createSampleTicket();
        updatedTicket.setSno(ticketId);
        updatedTicket.setDescription(newDescription); // Set new description
        updatedTicket.setPdfData("dummy_pdf_data".getBytes()); // Assume conversion generates data
        updatedTicket.setDocxData("dummy_docx_data".getBytes()); // Assume conversion generates data

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);

        Ticket result = ticketService.updateTicketDescription(ticketId, newDescription);

        assertNotNull(result);
        assertEquals(newDescription, result.getDescription());
        assertNotNull(result.getPdfData());
        assertNotNull(result.getDocxData());
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testUpdateTicketDescription_NotFound() {
        Long ticketId = 999L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.updateTicketDescription(ticketId, "Some text");
        });
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void testGetPdfDataById_Success() {
        Long ticketId = 1L;
        byte[] pdfData = "sample_pdf_data".getBytes();
        sampleTicket.setPdfData(pdfData); // Set the PDF data on the sample ticket
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(sampleTicket));

        byte[] result = ticketService.getPdfDataById(ticketId);

        assertNotNull(result);
        assertEquals("sample_pdf_data", new String(result));
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void testGetPdfDataById_NotFound() {
        Long ticketId = 999L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.getPdfDataById(ticketId);
        });
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void testGetDocxDataById_Success() {
        Long ticketId = 1L;
        byte[] docxData = "sample_docx_data".getBytes();
        sampleTicket.setDocxData(docxData); // Set the DOCX data on the sample ticket
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(sampleTicket));

        byte[] result = ticketService.getDocxDataById(ticketId);

        assertNotNull(result);
        assertEquals("sample_docx_data", new String(result));
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void testGetDocxDataById_NotFound() {
        Long ticketId = 999L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.getDocxDataById(ticketId);
        });
        verify(ticketRepository, times(1)).findById(ticketId);
    }


    private Ticket createSampleTicket() {
        Ticket ticket = new Ticket();
        ticket.setSno(1L); // Set an ID for consistency in tests
        ticket.setTitleTicket("Test Ticket");
        ticket.setAssign("John Doe");
        ticket.setStatus("Open");
        ticket.setPriority("High");
        ticket.setDateCurrent(LocalDate.now());
        ticket.setDescription("Test task description"); // Changed setSubTask to setDescription
        return ticket;
    }
}