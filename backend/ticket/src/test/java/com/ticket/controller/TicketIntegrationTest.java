package com.ticket.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.dto.TicketDTO;
import com.ticket.service.TicketService;

@WebMvcTest(TicketController.class)
public class TicketIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    private TicketDTO mockTicket;

    @BeforeEach
    void setUp() {
        mockTicket = new TicketDTO();
        mockTicket.setId(1L);
        mockTicket.setTitleTicket("Test Ticket");
        mockTicket.setDescription("Sample description");
    }

    @Test
    void testCreateTicket() throws Exception {
        when(ticketService.saveTicket(any(TicketDTO.class))).thenReturn(mockTicket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockTicket)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Ticket"));
    }

    @Test
    void testGetTicketById() throws Exception {
        when(ticketService.getTicketById(1L)).thenReturn(Optional.of(mockTicket));

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Ticket"));
    }

    @Test
    void testGetAllRootTickets() throws Exception {
        when(ticketService.getRootTickets()).thenReturn(List.of(mockTicket));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateTicket() throws Exception {
        mockTicket.setTitleTicket("Updated Title");
        when(ticketService.updateTicket(Mockito.eq(1L), any())).thenReturn(mockTicket);

        mockMvc.perform(put("/api/tickets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockTicket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testPatchTicketDescription() throws Exception {
        Map<String, String> patchMap = Map.of("description", "New Desc");
        mockTicket.setDescription("New Desc");

        when(ticketService.updateTicketDescription(1L, "New Desc")).thenReturn(mockTicket);

        mockMvc.perform(patch("/api/tickets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("New Desc"));
    }

    @Test
    void testDeleteTicket() throws Exception {
        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDownloadPdf() throws Exception {
        byte[] mockPdf = "PDF content".getBytes();
        when(ticketService.getPdfDataById(1L)).thenReturn(mockPdf);

        mockMvc.perform(get("/api/tickets/1/download-pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"));
    }

    @Test
    void testDownloadDocx() throws Exception {
        byte[] mockDocx = "DOCX content".getBytes();
        when(ticketService.getDocxDataById(1L)).thenReturn(mockDocx);

        mockMvc.perform(get("/api/tickets/1/download-docx"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/octet-stream"));
    }

    @Test
    void testGetSubTickets() throws Exception {
        TicketDTO subTicket = new TicketDTO();
        subTicket.setId(2L);
        subTicket.setTitleTicket("Subticket");

        when(ticketService.getSubTicketsByParent(1L)).thenReturn(List.of(subTicket));

        mockMvc.perform(get("/api/tickets/1/subtickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Subticket"));
    }

    @Test
    void testCreateSubTicket() throws Exception {
        TicketDTO subTicket = new TicketDTO();
        subTicket.setTitleTicket("Child Ticket");

        TicketDTO savedSub = new TicketDTO();
        savedSub.setId(99L);
        savedSub.setTitleTicket("Child Ticket");

        when(ticketService.getTicketById(1L)).thenReturn(Optional.of(mockTicket));
        when(ticketService.saveTicket(any())).thenReturn(savedSub);

        mockMvc.perform(post("/api/tickets/1/subtask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subTicket)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Child Ticket"))
                .andExpect(jsonPath("$.id").value(99L));
    }
}
