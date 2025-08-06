package com.ticket.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ticket.dto.TicketDTO;
import com.ticket.entity.Ticket;
import com.ticket.exception.TicketNotFoundException;
import com.ticket.mapper.TicketMapper;
import com.ticket.repository.TicketRepository;

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
        when(ticketRepository.save(any(Ticket.class))).thenReturn(sampleTicket);
        TicketDTO dto = TicketMapper.toDTO(sampleTicket);

        TicketDTO result = ticketService.saveTicket(dto);

        assertNotNull(result);
        assertEquals(dto.getTitleTicket(), result.getTitleTicket());
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testUpdateTicket_Success() {
        Long ticketId = 1L;
        Ticket updated = createSampleTicket();
        updated.setId(ticketId);
        updated.setTitleTicket("Updated");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(sampleTicket));
        when(ticketRepository.save(any())).thenReturn(updated);

        TicketDTO input = TicketMapper.toDTO(updated);
        TicketDTO result = ticketService.updateTicket(ticketId, input);

        assertEquals("Updated", result.getTitleTicket());
        verify(ticketRepository).findById(ticketId);
        verify(ticketRepository).save(any());
    }

    @Test
    void testUpdateTicket_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> ticketService.updateTicket(1L, TicketMapper.toDTO(sampleTicket)));
    }

    @Test
    void testUpdateTicketDescription_Success() {
        String desc = "<h1>Updated</h1>";
        Ticket updated = createSampleTicket();
        updated.setDescription(desc);
        updated.setPdfData("PDF".getBytes());
        updated.setDocxData("DOCX".getBytes());

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(sampleTicket));
        when(ticketRepository.save(any())).thenReturn(updated);

        TicketDTO result = ticketService.updateTicketDescription(1L, desc);

        assertEquals("Updated", Jsoup.parse(result.getDescription()).text());
        assertNotNull(result.getPdfData());
        assertNotNull(result.getDocxData());
    }

    @Test
    void testUpdateTicketDescription_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> ticketService.updateTicketDescription(1L, "some html"));
    }

    @Test
    void testGetTicketById_Success() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(sampleTicket));

        Optional<TicketDTO> result = ticketService.getTicketById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Ticket", result.get().getTitleTicket());
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TicketDTO> result = ticketService.getTicketById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteTicket_Success() {
        when(ticketRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ticketRepository).deleteById(1L);

        ticketService.deleteTicket(1L);

        verify(ticketRepository).deleteById(1L);
    }

    @Test
    void testDeleteTicket_NotFound() {
        when(ticketRepository.existsById(1L)).thenReturn(false);

        assertThrows(TicketNotFoundException.class,
                () -> ticketService.deleteTicket(1L));
    }

    @Test
    void testGetPdfDataById_Success() {
        byte[] data = "PDFDATA".getBytes();
        sampleTicket.setPdfData(data);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(sampleTicket));

        byte[] result = ticketService.getPdfDataById(1L);
        assertEquals("PDFDATA", new String(result));
    }

    @Test
    void testGetPdfDataById_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> ticketService.getPdfDataById(1L));
    }

    @Test
    void testGetDocxDataById_Success() {
        byte[] data = "DOCXDATA".getBytes();
        sampleTicket.setDocxData(data);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(sampleTicket));

        byte[] result = ticketService.getDocxDataById(1L);
        assertEquals("DOCXDATA", new String(result));
    }

    @Test
    void testGetDocxDataById_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> ticketService.getDocxDataById(1L));
    }

    @Test
    void testGetRootTickets_Success() {
        when(ticketRepository.findByParentTicketIsNull()).thenReturn(List.of(sampleTicket));

        List<TicketDTO> result = ticketService.getRootTickets();

        assertEquals(1, result.size());
        assertEquals("Test Ticket", result.get(0).getTitleTicket());
    }

    @Test
    void testGetSubTicketsByParent_Success() {
        Ticket parent = createSampleTicket();
        parent.setId(10L);

        Ticket child1 = createSampleTicket();
        child1.setId(11L);
        child1.setTitleTicket("Sub1");

        Ticket child2 = createSampleTicket();
        child2.setId(12L);
        child2.setTitleTicket("Sub2");

        when(ticketRepository.findById(10L)).thenReturn(Optional.of(parent));
        when(ticketRepository.findByParentTicket(parent)).thenReturn(List.of(child1, child2));

        List<TicketDTO> result = ticketService.getSubTicketsByParent(10L);

        assertEquals(2, result.size());
        assertEquals("Sub1", result.get(0).getTitleTicket());
        assertEquals("Sub2", result.get(1).getTitleTicket());
    }

    @Test
    void testGetSubTicketsByParent_NotFound() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> ticketService.getSubTicketsByParent(10L));
    }

    // Utility to create test Ticket
    private Ticket createSampleTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitleTicket("Test Ticket");
        ticket.setAssign("John Doe");
        ticket.setStatus("Open");
        ticket.setPriority("High");
        ticket.setDescription("Test description");
        ticket.setDateCurrent(LocalDate.now());
        return ticket;
    }
}
