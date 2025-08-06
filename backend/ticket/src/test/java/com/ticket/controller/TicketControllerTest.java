//package com.ticket.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.ticket.entity.Ticket;
//import com.ticket.repository.TicketRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.LocalDate;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class TicketControllerTest {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Autowired
//    private TicketRepository ticketRepository;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        ticketRepository.deleteAll();
//    }
//
//    @Test
//    void testCreateTicket_Success() throws Exception {
//        Ticket ticket = createSampleTicket();
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.titleTicket").value("Test Ticket"))
//                .andExpect(jsonPath("$.assign").value("John Doe"))
//                .andExpect(jsonPath("$.status").value("Open"))
//                .andExpect(jsonPath("$.priority").value("High"))
//                .andExpect(jsonPath("$.subTask").value("Test task"));
//    }
//
//    @Test
//    void testCreateTicket_ValidationError_MissingTitle() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setTitleTicket(null);
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.titleTicket").value("Title is required"));
//    }
//
//    @Test
//    void testCreateTicket_ValidationError_MissingAssignee() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setAssign(null);
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.assign").value("Assignee is required"));
//    }
//
//    @Test
//    void testCreateTicket_ValidationError_MissingStatus() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setStatus(null);
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value("Status is required"));
//    }
//
//    @Test
//    void testCreateTicket_ValidationError_MissingPriority() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setPriority(null);
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.priority").value("Priority is required"));
//    }
//
//    @Test
//    void testCreateTicket_ValidationError_MissingDate() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setDateCurrent(null);
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isCreated()) // Should succeed now since date is auto-set
//                .andExpect(jsonPath("$.dateCurrent").exists()); // Date should be automatically set
//    }
//
//    @Test
//    void testCreateTicket_WithoutDate() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setDateCurrent(null);
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.dateCurrent").exists())
//                .andExpect(jsonPath("$.titleTicket").value("Test Ticket"));
//    }
//
//    @Test
//    void testCreateTicket_WithProvidedDate() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setDateCurrent(LocalDate.of(2024, 1, 15));
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.dateCurrent").value("2024-01-15"));
//    }
//
//    @Test
//    void testGetAllTickets_Success() throws Exception {
//        // Create test tickets
//        Ticket ticket1 = createSampleTicket();
//        ticket1.setTitleTicket("Ticket 1");
//        ticketRepository.save(ticket1);
//
//        Ticket ticket2 = createSampleTicket();
//        ticket2.setTitleTicket("Ticket 2");
//        ticketRepository.save(ticket2);
//
//        mockMvc.perform(get("/api/tickets"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].titleTicket").value("Ticket 1"))
//                .andExpect(jsonPath("$[1].titleTicket").value("Ticket 2"))
//                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(2)));
//    }
//
//    @Test
//    void testGetAllTickets_EmptyList() throws Exception {
//        mockMvc.perform(get("/api/tickets"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(0)));
//    }
//
//    @Test
//    void testGetTicketById_Success() throws Exception {
//        Ticket ticket = createSampleTicket();
//        Ticket savedTicket = ticketRepository.save(ticket);
//
//        mockMvc.perform(get("/api/tickets/" + savedTicket.getSno()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.titleTicket").value("Test Ticket"))
//                .andExpect(jsonPath("$.assign").value("John Doe"))
//                .andExpect(jsonPath("$.status").value("Open"))
//                .andExpect(jsonPath("$.priority").value("High"));
//    }
//
//    @Test
//    void testGetTicketById_NotFound() throws Exception {
//        mockMvc.perform(get("/api/tickets/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("Ticket not found with id: 999"));
//    }
//
//    @Test
//    void testUpdateTicket_Success() throws Exception {
//        Ticket ticket = createSampleTicket();
//        Ticket savedTicket = ticketRepository.save(ticket);
//
//        Ticket updateTicket = createSampleTicket();
//        updateTicket.setTitleTicket("Updated Ticket");
//        updateTicket.setStatus("In Progress");
//        String updateJson = objectMapper.writeValueAsString(updateTicket);
//
//        mockMvc.perform(put("/api/tickets/" + savedTicket.getSno())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(updateJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.titleTicket").value("Updated Ticket"))
//                .andExpect(jsonPath("$.status").value("In Progress"));
//    }
//
//    @Test
//    void testUpdateTicket_NotFound() throws Exception {
//        Ticket updateTicket = createSampleTicket();
//        String updateJson = objectMapper.writeValueAsString(updateTicket);
//
//        mockMvc.perform(put("/api/tickets/999")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(updateJson))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("Ticket not found with id: 999"));
//    }
//
//    @Test
//    void testUpdateTicket_ValidationError() throws Exception {
//        Ticket ticket = createSampleTicket();
//        Ticket savedTicket = ticketRepository.save(ticket);
//
//        Ticket updateTicket = createSampleTicket();
//        updateTicket.setTitleTicket(null); // Invalid
//        String updateJson = objectMapper.writeValueAsString(updateTicket);
//
//        mockMvc.perform(put("/api/tickets/" + savedTicket.getSno())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(updateJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.titleTicket").value("Title is required"));
//    }
//
//    @Test
//    void testDeleteTicket_Success() throws Exception {
//        Ticket ticket = createSampleTicket();
//        Ticket savedTicket = ticketRepository.save(ticket);
//
//        mockMvc.perform(delete("/api/tickets/" + savedTicket.getSno()))
//                .andExpect(status().isNoContent());
//
//        // Verify ticket is deleted
//        mockMvc.perform(get("/api/tickets/" + savedTicket.getSno()))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testDeleteTicket_NotFound() throws Exception {
//        mockMvc.perform(delete("/api/tickets/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("Ticket not found with id: 999"));
//    }
//
//    @Test
//    void testCreateTicket_WithOptionalFields() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setSubTask("Optional sub task");
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.subTask").value("Optional sub task"));
//    }
//
//    @Test
//    void testCreateTicket_WithoutOptionalFields() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setSubTask(null);
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.subTask").isEmpty());
//    }
//
//    @Test
//    void testCreateTicket_DateSerialization() throws Exception {
//        Ticket ticket = createSampleTicket();
//        ticket.setDateCurrent(LocalDate.of(2024, 1, 15));
//        String ticketJson = objectMapper.writeValueAsString(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(ticketJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.dateCurrent").value("2024-01-15"));
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



package com.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ticket.entity.Ticket;
import com.ticket.repository.TicketRepository;
import org.junit.jupiter.api.AfterEach; // Added for cleanup consistency
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TicketControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TicketRepository ticketRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ticketRepository.deleteAll(); // Ensure a clean state before each test
    }

    @AfterEach // Added for cleanup after each test
    void tearDown() {
        ticketRepository.deleteAll();
    }

    @Test
    void testCreateTicket_Success() throws Exception {
        Ticket ticket = createSampleTicket();
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titleTicket").value("Test Ticket"))
                .andExpect(jsonPath("$.assign").value("John Doe"))
                .andExpect(jsonPath("$.status").value("Open"))
                .andExpect(jsonPath("$.priority").value("High"))
                .andExpect(jsonPath("$.description").value("Test task description")); // Changed subTask to description
    }

    @Test
    void testCreateTicket_ValidationError_MissingTitle() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setTitleTicket(null);
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.titleTicket").value("Title is required"));
    }

    @Test
    void testCreateTicket_ValidationError_MissingAssignee() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setAssign(null);
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.assign").value("Assignee is required"));
    }

    @Test
    void testCreateTicket_ValidationError_MissingStatus() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setStatus(null);
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Status is required"));
    }

    @Test
    void testCreateTicket_ValidationError_MissingPriority() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setPriority(null);
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.priority").value("Priority is required"));
    }

    @Test
    void testCreateTicket_ValidationError_MissingDate() throws Exception {
        // Note: With @PrePersist on dateCurrent, this test may pass as date will be auto-set.
        // It tests if other validation errors occur if date is NOT auto-set and required.
        // If dateCurrent is nullable, it would just be null.
        // As per entity, dateCurrent is @Column(nullable = false), so @PrePersist ensures it.
        // This test now verifies that it IS created even if not provided.
        Ticket ticket = createSampleTicket();
        ticket.setDateCurrent(null);
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isCreated()) // Should succeed now since date is auto-set
                .andExpect(jsonPath("$.dateCurrent").exists()); // Date should be automatically set
    }

    @Test
    void testCreateTicket_WithoutDate() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setDateCurrent(null); // Explicitly set to null to test auto-setting
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dateCurrent").exists()) // Check that date was auto-set
                .andExpect(jsonPath("$.titleTicket").value("Test Ticket"));
    }

    @Test
    void testCreateTicket_WithProvidedDate() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setDateCurrent(LocalDate.of(2024, 1, 15));
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dateCurrent").value("2024-01-15"));
    }

    @Test
    void testGetAllTickets_Success() throws Exception {
        // Create test tickets
        Ticket ticket1 = createSampleTicket();
        ticket1.setTitleTicket("Ticket 1");
        ticketRepository.save(ticket1);

        Ticket ticket2 = createSampleTicket();
        ticket2.setTitleTicket("Ticket 2");
        ticketRepository.save(ticket2);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titleTicket").value("Ticket 1"))
                .andExpect(jsonPath("$[1].titleTicket").value("Ticket 2"))
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(2)));
    }

    @Test
    void testGetAllTickets_EmptyList() throws Exception {
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(0)));
    }

    @Test
    void testGetTicketById_Success() throws Exception {
        Ticket ticket = createSampleTicket();
        Ticket savedTicket = ticketRepository.save(ticket);

        mockMvc.perform(get("/api/tickets/" + savedTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titleTicket").value("Test Ticket"))
                .andExpect(jsonPath("$.assign").value("John Doe"))
                .andExpect(jsonPath("$.status").value("Open"))
                .andExpect(jsonPath("$.priority").value("High"));
    }

    @Test
    void testGetTicketById_NotFound() throws Exception {
        mockMvc.perform(get("/api/tickets/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Ticket not found with id: 999"));
    }

    @Test
    void testUpdateTicket_Success() throws Exception {
        Ticket ticket = createSampleTicket();
        Ticket savedTicket = ticketRepository.save(ticket);

        Ticket updateTicket = createSampleTicket();
        updateTicket.setTitleTicket("Updated Ticket");
        updateTicket.setStatus("In Progress");
        updateTicket.setPriority("Medium");
        updateTicket.setDescription("Updated task description"); // Changed setSubTask to setDescription
        String updateJson = objectMapper.writeValueAsString(updateTicket);

        mockMvc.perform(put("/api/tickets/" + savedTicket.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titleTicket").value("Updated Ticket"))
                .andExpect(jsonPath("$.status").value("In Progress"))
                .andExpect(jsonPath("$.priority").value("Medium"))
                .andExpect(jsonPath("$.description").value("Updated task description")); // Changed subTask to description
    }

    @Test
    void testUpdateTicket_NotFound() throws Exception {
        Ticket updateTicket = createSampleTicket();
        String updateJson = objectMapper.writeValueAsString(updateTicket);

        mockMvc.perform(put("/api/tickets/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Ticket not found with id: 999"));
    }

    @Test
    void testUpdateTicket_ValidationError() throws Exception {
        Ticket ticket = createSampleTicket();
        Ticket savedTicket = ticketRepository.save(ticket);

        Ticket updateTicket = createSampleTicket();
        updateTicket.setTitleTicket(null); // Invalid
        String updateJson = objectMapper.writeValueAsString(updateTicket);

        mockMvc.perform(put("/api/tickets/" + savedTicket.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.titleTicket").value("Title is required"));
    }

    @Test
    void testDeleteTicket_Success() throws Exception {
        Ticket ticket = createSampleTicket();
        Ticket savedTicket = ticketRepository.save(ticket);

        mockMvc.perform(delete("/api/tickets/" + savedTicket.getId()))
                .andExpect(status().isNoContent());

        // Verify ticket is deleted
        mockMvc.perform(get("/api/tickets/" + savedTicket.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTicket_NotFound() throws Exception {
        mockMvc.perform(delete("/api/tickets/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Ticket not found with id: 999"));
    }

    @Test
    void testCreateTicket_WithOptionalFields() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setDescription("Optional description"); // Changed setSubTask to setDescription
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Optional description")); // Changed subTask to description
    }

    @Test
    void testCreateTicket_WithoutOptionalFields() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setDescription(null); // Changed setSubTask to setDescription
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").isEmpty()); // Changed subTask to description
    }

    @Test
    void testCreateTicket_DateSerialization() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setDateCurrent(LocalDate.of(2024, 1, 15));
        String ticketJson = objectMapper.writeValueAsString(ticket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dateCurrent").value("2024-01-15"));
    }

    @Test
    void testDownloadPdf() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setPdfData("dummy_pdf_content".getBytes()); // Set dummy PDF data
        Ticket savedTicket = ticketRepository.save(ticket);

        mockMvc.perform(get("/api/tickets/{id}/download-pdf", savedTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_PDF_VALUE))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"ticket_" + savedTicket.getId() + ".pdf\""))
                .andExpect(content().bytes("dummy_pdf_content".getBytes()));
    }

    @Test
    void testDownloadPdf_NotFound() throws Exception {
        mockMvc.perform(get("/api/tickets/999/download-pdf"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("PDF data not found for ticket id: 999"));
    }

    @Test
    void testDownloadDocx() throws Exception {
        Ticket ticket = createSampleTicket();
        ticket.setDocxData("dummy_docx_content".getBytes()); // Set dummy DOCX data
        Ticket savedTicket = ticketRepository.save(ticket);

        mockMvc.perform(get("/api/tickets/{id}/download-docx", savedTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"ticket_" + savedTicket.getId() + ".docx\""))
                .andExpect(content().bytes("dummy_docx_content".getBytes()));
    }

    @Test
    void testDownloadDocx_NotFound() throws Exception {
        mockMvc.perform(get("/api/tickets/999/download-docx"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("DOCX data not found for ticket id: 999"));
    }

    @Test
    void testPatchDescription() throws Exception {
        Ticket ticket = createSampleTicket();
        Ticket savedTicket = ticketRepository.save(ticket);
        String newDescription = "Patched description via PATCH";

        mockMvc.perform(patch("/api/tickets/{id}", savedTicket.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.Collections.singletonMap("description", newDescription))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(newDescription));

        // Verify it's updated in DB
        Ticket updatedTicket = ticketRepository.findById(savedTicket.getId()).orElseThrow();
        assertEquals(newDescription, updatedTicket.getDescription());
    }

    @Test
    void testPatchDescription_NotFound() throws Exception {
        String newDescription = "Patched description via PATCH";

        mockMvc.perform(patch("/api/tickets/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.Collections.singletonMap("description", newDescription))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Ticket not found with id: 999"));
    }

    private Ticket createSampleTicket() {
        Ticket ticket = new Ticket();
        ticket.setTitleTicket("Test Ticket");
        ticket.setAssign("John Doe");
        ticket.setStatus("Open");
        ticket.setPriority("High");
        ticket.setDateCurrent(LocalDate.now());
        ticket.setDescription("Test task");
        return ticket;
    }
}