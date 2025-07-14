//package com.ticket.service;
//
//import com.ticket.entity.Ticket;
//import com.ticket.exception.TicketNotFoundException;
//import com.ticket.repository.TicketRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class TicketService {
//
//    private final TicketRepository ticketRepository;
//
//    public TicketService(TicketRepository ticketRepository) {
//        this.ticketRepository = ticketRepository;
//    }
//
//    public Ticket saveTicket(Ticket ticket) {
//        try {
//            // Only save the ticket, no subtask logic
//            return ticketRepository.save(ticket);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to save ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public List<Ticket> getAllTickets() {
//        try {
//            return ticketRepository.findAll();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve tickets: " + e.getMessage(), e);
//        }
//    }
//
//    public Optional<Ticket> getTicketById(Long id) {
//        try {
//            return ticketRepository.findById(id);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public Ticket updateTicket(Long id, Ticket updatedTicket) {
//        try {
//            return ticketRepository.findById(id)
//                    .map(existingTicket -> {
//                        existingTicket.setTitleTicket(updatedTicket.getTitleTicket());
//                        existingTicket.setAssign(updatedTicket.getAssign());
//                        existingTicket.setStatus(updatedTicket.getStatus());
//                        existingTicket.setPriority(updatedTicket.getPriority());
//                        if (updatedTicket.getDateCurrent() != null) {
//                            existingTicket.setDateCurrent(updatedTicket.getDateCurrent());
//                        }
//                        existingTicket.setSubTask(updatedTicket.getSubTask());
//                        // Remove subTask and subTasks logic
//                        return ticketRepository.save(existingTicket);
//                    })
//                    .orElseThrow(() -> new TicketNotFoundException(id));
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to update ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public void deleteTicket(Long id) {
//        try {
//            if (!ticketRepository.existsById(id)) {
//                throw new TicketNotFoundException(id);
//            }
//            ticketRepository.deleteById(id);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to delete ticket: " + e.getMessage(), e);
//        }
//    }
//}



//===================================================================================

//
//package com.ticket.service;
//
//import com.ticket.entity.Ticket;
//import com.ticket.exception.TicketNotFoundException;
//import com.ticket.repository.TicketRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class TicketService {
//
//    private final TicketRepository ticketRepository;
//
//    public TicketService(TicketRepository ticketRepository) {
//        this.ticketRepository = ticketRepository;
//    }
//
//    public Ticket saveTicket(Ticket ticket) {
//        try {
//            return ticketRepository.save(ticket);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to save ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public List<Ticket> getAllTickets() {
//        try {
//            return ticketRepository.findAll();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve tickets: " + e.getMessage(), e);
//        }
//    }
//
//    public Optional<Ticket> getTicketById(Long id) {
//        try {
//            return ticketRepository.findById(id);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public Ticket updateTicket(Long id, Ticket updatedTicket) {
//        try {
//            return ticketRepository.findById(id)
//                    .map(existingTicket -> {
//                        existingTicket.setTitleTicket(updatedTicket.getTitleTicket());
//                        existingTicket.setAssign(updatedTicket.getAssign());
//                        existingTicket.setStatus(updatedTicket.getStatus());
//                        existingTicket.setPriority(updatedTicket.getPriority());
//                        if (updatedTicket.getDateCurrent() != null) {
//                            existingTicket.setDateCurrent(updatedTicket.getDateCurrent());
//                        }
//                        // Update description field, not subTask
//                        existingTicket.setDescription(updatedTicket.getDescription());
//                        return ticketRepository.save(existingTicket);
//                    })
//                    .orElseThrow(() -> new TicketNotFoundException(id));
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to update ticket: " + e.getMessage(), e);
//        }
//    }
//
//    // New method for partial update of description
//    public Ticket updateTicketDescription(Long id, String description) {
//        return ticketRepository.findById(id)
//                .map(ticket -> {
//                    ticket.setDescription(description);
//                    return ticketRepository.save(ticket);
//                })
//                .orElseThrow(() -> new TicketNotFoundException(id));
//    }
//
//    public void deleteTicket(Long id) {
//        try {
//            if (!ticketRepository.existsById(id)) {
//                throw new TicketNotFoundException(id);
//            }
//            ticketRepository.deleteById(id);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to delete ticket: " + e.getMessage(), e);
//        }
//    }
//}
//





//=============================================================================
//package com.ticket.service;
//
//import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
//import com.ticket.entity.Ticket;
//import com.ticket.exception.TicketNotFoundException;
//import com.ticket.repository.TicketRepository;
//
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.ByteArrayOutputStream;
//import java.util.List;
//import java.util.Optional;
//
//// --- Conversion Library Imports (EXAMPLES - You need to add these dependencies) ---
//// For PDF:
//// import com.openhtmltopdf.pdfbox.PdfRendererBuilder; // Example for OpenHTMLToPDF
//// import java.io.ByteArrayOutputStream;
//
//// For DOCX:
//// import org.apache.poi.xwpf.usermodel.XWPFDocument; // Example for Apache POI
//// import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//// import org.apache.poi.xwpf.usermodel.XWPFRun;
//// import java.io.ByteArrayInputStream;
//// import org.jsoup.Jsoup; // To parse HTML
//// import org.jsoup.nodes.Document;
//// import org.jsoup.nodes.Element;
//// import org.jsoup.select.Elements;
//// ----------------------------------------------------------------------------------
//
//@Service
//@Transactional
//public class TicketService {
//
//    private final TicketRepository ticketRepository;
//
//    public TicketService(TicketRepository ticketRepository) {
//        this.ticketRepository = ticketRepository;
//    }
//
//    public Ticket saveTicket(Ticket ticket) {
//        try {
//            return ticketRepository.save(ticket);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to save ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public List<Ticket> getAllTickets() {
//        try {
//            return ticketRepository.findAll();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve tickets: " + e.getMessage(), e);
//        }
//    }
//
//    public Optional<Ticket> getTicketById(Long id) {
//        try {
//            return ticketRepository.findById(id);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public Ticket updateTicket(Long id, Ticket updatedTicket) {
//        try {
//            return ticketRepository.findById(id)
//                    .map(existingTicket -> {
//                        existingTicket.setTitleTicket(updatedTicket.getTitleTicket());
//                        existingTicket.setAssign(updatedTicket.getAssign());
//                        existingTicket.setStatus(updatedTicket.getStatus());
//                        existingTicket.setPriority(updatedTicket.getPriority());
//                        if (updatedTicket.getDateCurrent() != null) {
//                            existingTicket.setDateCurrent(updatedTicket.getDateCurrent());
//                        }
//                        existingTicket.setDescription(updatedTicket.getDescription());
//                        // Assigning binary data directly (e.g., if re-uploading entire files)
//                        existingTicket.setPdfData(updatedTicket.getPdfData());
//                        existingTicket.setDocxData(updatedTicket.getDocxData());
//
//                        return ticketRepository.save(existingTicket);
//                    })
//                    .orElseThrow(() -> new TicketNotFoundException(id));
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to update ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public Ticket updateTicketDescription(Long id, String description) {
//        return ticketRepository.findById(id)
//                .map(ticket -> {
//                    ticket.setDescription(description);
//
//                    // --- HTML to PDF/DOCX Conversion (Placeholder Logic) ---
//                    // This is where you would call your conversion methods.
//                    // For a real application, you'd need to add libraries and robust conversion logic.
//                    try {
//                        byte[] pdfBytes = convertHtmlToPdf(description); // Implement this method
//                        ticket.setPdfData(pdfBytes);
//                    } catch (Exception e) {
//                        System.err.println("Failed to convert HTML to PDF: " + e.getMessage());
//                        // Handle conversion failure, e.g., log it, but don't prevent saving description
//                        ticket.setPdfData(null); // Clear old PDF if conversion fails
//                    }
//
//                    try {
//                        byte[] docxBytes = convertHtmlToDocx(description); // Implement this method
//                        ticket.setDocxData(docxBytes);
//                    } catch (Exception e) {
//                        System.err.println("Failed to convert HTML to DOCX: " + e.getMessage());
//                        // Handle conversion failure
//                        ticket.setDocxData(null); // Clear old DOCX if conversion fails
//                    }
//                    // --------------------------------------------------------
//
//                    return ticketRepository.save(ticket);
//                })
//                .orElseThrow(() -> new TicketNotFoundException(id));
//    }
//
//    // Placeholder for HTML to PDF conversion
//    // You MUST add OpenHTMLToPDF or iText dependencies for this to work
//    private byte[] convertHtmlToPdf(String htmlContent) throws Exception {
//        // --- Dummy implementation for now ---
//        // For a real implementation, add the following to your pom.xml:
//        /*
//        <dependency>
//            <groupId>com.openhtmltopdf</groupId>
//            <artifactId>openhtmltopdf-pdfbox</artifactId>
//            <version>1.0.10</version>
//        </dependency>
//        <dependency>
//            <groupId>com.openhtmltopdf</groupId>
//            <artifactId>openhtmltopdf-rtl-support</artifactId>
//            <version>1.0.10</version>
//        </dependency>
//        */
//        // And then uncomment the actual conversion logic below:
//        /*
//        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
//            PdfRendererBuilder builder = new PdfRendererBuilder();
//            builder.withHtmlContent(htmlContent, null); // Base URL can be null or a real path if HTML has relative paths
//            builder.toStream(os);
//            builder.run();
//            return os.toByteArray();
//        }
//        */
//    	
//    	try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
//            PdfRendererBuilder builder = new PdfRendererBuilder();
//            builder.withHtmlContent(htmlContent, null); // Base URL can be null or a real path if HTML has relative paths
//            builder.toStream(os);
//            builder.run();
//            return os.toByteArray();
//        // For now, return a dummy byte array or throw a custom exception
//        System.out.println("Dummy PDF conversion triggered. (No actual PDF generated without library)");
//        return ("Dummy PDF content for: " + htmlContent).getBytes(); // Placeholder
//    }
//
//    // Placeholder for HTML to DOCX conversion
//    // You MUST add Apache POI and Jsoup dependencies for this to work
//    private byte[] convertHtmlToDocx(String htmlContent) throws Exception {
//        // --- Dummy implementation for now ---
//        // For a real implementation, add the following to your pom.xml:
//        /*
//        <dependency>
//            <groupId>org.apache.poi</groupId>
//            <artifactId>poi-ooxml</artifactId>
//            <version>5.2.5</version>
//        </dependency>
//        <dependency>
//            <groupId>org.jsoup</groupId>
//            <artifactId>jsoup</artifactId>
//            <version>1.17.2</version>
//        </dependency>
//        */
//        // And then uncomment the actual conversion logic below (basic conversion):
//        /*
//        try (XWPFDocument document = new XWPFDocument();
//             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
//
//            Document htmlDoc = Jsoup.parse(htmlContent);
//
//            for (Element element : htmlDoc.body().children()) {
//                XWPFParagraph paragraph = document.createParagraph();
//                XWPFRun run = paragraph.createRun();
//
//                if (element.tagName().equals("p")) {
//                    run.setText(element.text());
//                } else if (element.tagName().equals("h1")) {
//                    run.setText(element.text());
//                    run.setBold(true);
//                    run.setFontSize(24);
//                }
//                // ... handle other tags like ul, ol, img etc. this is complex
//            }
//
//            document.write(bos);
//            return bos.toByteArray();
//        }
//        */
//    	
//    	try (XWPFDocument document = new XWPFDocument();
//                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
//
//               Document htmlDoc = Jsoup.parse(htmlContent);
//
//               for (Element element : htmlDoc.body().children()) {
//                   XWPFParagraph paragraph = document.createParagraph();
//                   XWPFRun run = paragraph.createRun();
//
//                   if (element.tagName().equals("p")) {
//                       run.setText(element.text());
//                   } else if (element.tagName().equals("h1")) {
//                       run.setText(element.text());
//                       run.setBold(true);
//                       run.setFontSize(24);
//                   }
//                   // ... handle other tags like ul, ol, img etc. this is complex
//               }
//
//               document.write(bos);
//               return bos.toByteArray();
//           }
//    }
//
//
//    public byte[] getPdfDataById(Long id) {
//        return ticketRepository.findById(id)
//                .map(Ticket::getPdfData)
//                .orElseThrow(() -> new TicketNotFoundException("PDF data not found for ticket id: " + id));
//    }
//
//    public byte[] getDocxDataById(Long id) {
//        return ticketRepository.findById(id)
//                .map(Ticket::getDocxData)
//                .orElseThrow(() -> new TicketNotFoundException("DOCX data not found for ticket id: " + id));
//    }
//
//    public void deleteTicket(Long id) {
//        try {
//            if (!ticketRepository.existsById(id)) {
//                throw new TicketNotFoundException(id);
//            }
//            ticketRepository.deleteById(id);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to delete ticket: " + e.getMessage(), e);
//        }
//    }
//}



//===============================
//package com.ticket.service;
//
////import com.openhtmltopdf.pdfbox.PdfRendererBuilder; // This import should now be resolved
//import com.ticket.entity.Ticket;
//import com.ticket.exception.TicketNotFoundException;
//import com.ticket.repository.TicketRepository;
//
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.ByteArrayOutputStream;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class TicketService {
//
//    private final TicketRepository ticketRepository;
//
//    public TicketService(TicketRepository ticketRepository) {
//        this.ticketRepository = ticketRepository;
//    }
//
//    public Ticket saveTicket(Ticket ticket) {
//        try {
//            return ticketRepository.save(ticket);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to save ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public List<Ticket> getAllTickets() {
//        try {
//            return ticketRepository.findAll();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve tickets: " + e.getMessage(), e);
//        }
//    }
//
//    public Optional<Ticket> getTicketById(Long id) {
//        try {
//            return ticketRepository.findById(id);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public Ticket updateTicket(Long id, Ticket updatedTicket) {
//        try {
//            return ticketRepository.findById(id)
//                    .map(existingTicket -> {
//                        existingTicket.setTitleTicket(updatedTicket.getTitleTicket());
//                        existingTicket.setAssign(updatedTicket.getAssign());
//                        existingTicket.setStatus(updatedTicket.getStatus());
//                        existingTicket.setPriority(updatedTicket.getPriority());
//                        if (updatedTicket.getDateCurrent() != null) {
//                            existingTicket.setDateCurrent(updatedTicket.getDateCurrent());
//                        }
//                        existingTicket.setDescription(updatedTicket.getDescription());
//                        existingTicket.setPdfData(updatedTicket.getPdfData());
//                        existingTicket.setDocxData(updatedTicket.getDocxData());
//
//                        return ticketRepository.save(existingTicket);
//                    })
//                    .orElseThrow(() -> new TicketNotFoundException(id));
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to update ticket: " + e.getMessage(), e);
//        }
//    }
//
//    public Ticket updateTicketDescription(Long id, String description) {
//        return ticketRepository.findById(id)
//                .map(ticket -> {
//                    ticket.setDescription(description);
//
//                    // --- HTML to PDF/DOCX Conversion Logic ---
//                    try {
//                        byte[] pdfBytes = convertHtmlToPdf(description);
//                        ticket.setPdfData(pdfBytes);
//                    } catch (Exception e) {
//                        System.err.println("Failed to convert HTML to PDF for ticket " + id + ": " + e.getMessage());
//                        e.printStackTrace();
//                        ticket.setPdfData(null);
//                    }
//
//                    try {
//                        byte[] docxBytes = convertHtmlToDocx(description);
//                        ticket.setDocxData(docxBytes);
//                    } catch (Exception e) {
//                        System.err.println("Failed to convert HTML to DOCX for ticket " + id + ": " + e.getMessage());
//                        e.printStackTrace();
//                        ticket.setDocxData(null);
//                    }
//                    // --------------------------------------------------------
//
//                    return ticketRepository.save(ticket);
//                })
//                .orElseThrow(() -> new TicketNotFoundException(id));
//    }
//
//    // HTML to PDF conversion using OpenHTMLToPDF
//    private byte[] convertHtmlToPdf(String htmlContent) throws Exception {
//        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
//            com.openhtmltopdf.pdfboxout.PdfRendererBuilder builder = new com.openhtmltopdf.pdfboxout.PdfRendererBuilder();
//            builder.withHtmlContent(htmlContent, null);
//            builder.toStream(os);
//            builder.run();
//            return os.toByteArray();
//        }
//    }
//
//    // HTML to DOCX conversion using Apache POI and Jsoup (simplified)
//    private byte[] convertHtmlToDocx(String htmlContent) throws Exception {
//        try (XWPFDocument document = new XWPFDocument();
//             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
//
//            Document htmlDoc = Jsoup.parse(htmlContent);
//
//            for (Element element : htmlDoc.body().children()) {
//                XWPFParagraph paragraph = document.createParagraph();
//                XWPFRun run = paragraph.createRun();
//
//                if (element.tagName().equals("p")) {
//                    run.setText(element.text());
//                } else if (element.tagName().equals("h1")) {
//                    run.setText(element.text());
//                    run.setBold(true);
//                    run.setFontSize(24);
//                } else if (element.tagName().equals("h2")) {
//                    run.setText(element.text());
//                    run.setBold(true);
//                    run.setFontSize(18);
//                } else if (element.tagName().equals("h3")) {
//                    run.setText(element.text());
//                    run.setBold(true);
//                    run.setFontSize(14);
//                } else if (element.tagName().equals("strong") || element.tagName().equals("b")) {
//                    run.setText(element.text());
//                    run.setBold(true);
//                } else if (element.tagName().equals("em") || element.tagName().equals("i")) {
//                    run.setText(element.text());
//                    run.setItalic(true);
//                } else if (element.tagName().equals("ul") || element.tagName().equals("ol")) {
//                    for (Element li : element.children()) {
//                        XWPFParagraph listItemParagraph = document.createParagraph();
//                        XWPFRun listItemRun = listItemParagraph.createRun();
//                        if (element.tagName().equals("ul")) {
//                            listItemRun.setText("• " + li.text());
//                        } else {
//                            listItemRun.setText(li.text());
//                        }
//                    }
//                }
//                else {
//                    run.setText(element.text());
//                }
//            }
//
//            document.write(bos);
//            return bos.toByteArray();
//        }
//    }
//
//    public byte[] getPdfDataById(Long id) {
//        return ticketRepository.findById(id)
//                .map(Ticket::getPdfData)
//                .orElseThrow(() -> new TicketNotFoundException("PDF data not found for ticket id: " + id));
//    }
//
//    public byte[] getDocxDataById(Long id) {
//        return ticketRepository.findById(id)
//                .map(Ticket::getDocxData)
//                .orElseThrow(() -> new TicketNotFoundException("DOCX data not found for ticket id: " + id));
//    }
//
//    public void deleteTicket(Long id) {
//        try {
//            if (!ticketRepository.existsById(id)) {
//                throw new TicketNotFoundException(id);
//            }
//            ticketRepository.deleteById(id);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to delete ticket: " + e.getMessage(), e);
//        }
//    }
//}



//==========================
package com.ticket.service;

import com.ticket.entity.Ticket;
import com.ticket.exception.TicketNotFoundException;
import com.ticket.repository.TicketRepository;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket saveTicket(Ticket ticket) {
        try {
            return ticketRepository.save(ticket);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save ticket: " + e.getMessage(), e);
        }
    }

    public List<Ticket> getAllTickets() {
        try {
            return ticketRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    public Optional<Ticket> getTicketById(Long id) {
        try {
            return ticketRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve ticket: " + e.getMessage(), e);
        }
    }

    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        try {
            return ticketRepository.findById(id)
                    .map(existingTicket -> {
                        existingTicket.setTitleTicket(updatedTicket.getTitleTicket());
                        existingTicket.setAssign(updatedTicket.getAssign());
                        existingTicket.setStatus(updatedTicket.getStatus());
                        existingTicket.setPriority(updatedTicket.getPriority());
                        if (updatedTicket.getDateCurrent() != null) {
                            existingTicket.setDateCurrent(updatedTicket.getDateCurrent());
                        }
                        existingTicket.setDescription(updatedTicket.getDescription());
                        existingTicket.setPdfData(updatedTicket.getPdfData());
                        existingTicket.setDocxData(updatedTicket.getDocxData());

                        return ticketRepository.save(existingTicket);
                    })
                    .orElseThrow(() -> new TicketNotFoundException(id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to update ticket: " + e.getMessage(), e);
        }
    }

    public Ticket updateTicketDescription(Long id, String description) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setDescription(description);

                    // --- HTML to PDF/DOCX Conversion Logic ---
                    try {
                        byte[] pdfBytes = convertHtmlToPdf(description);
                        ticket.setPdfData(pdfBytes);
                    } catch (Exception e) {
                        System.err.println("Failed to convert HTML to PDF for ticket " + id + ": " + e.getMessage());
                        e.printStackTrace();
                        ticket.setPdfData(null);
                    }

                    try {
                        byte[] docxBytes = convertHtmlToDocx(description);
                        ticket.setDocxData(docxBytes);
                    } catch (Exception e) {
                        System.err.println("Failed to convert HTML to DOCX for ticket " + id + ": " + e.getMessage());
                        e.printStackTrace();
                        ticket.setDocxData(null);
                    }
                    // --------------------------------------------------------

                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    // HTML to PDF conversion using OpenHTMLToPDF
    private byte[] convertHtmlToPdf(String htmlContent) throws Exception {
        // Use Jsoup to parse and clean the HTML, converting named entities to numeric
        // This helps the XML parser handle entities like &nbsp;
        Document doc = Jsoup.parse(htmlContent);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml); // Output as XML syntax
        doc.outputSettings().charset("UTF-8");
        // Ensure that HTML entities are escaped to numeric entities for XML parsers
        doc.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml); 
        String cleanedHtml = doc.html();

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            com.openhtmltopdf.pdfboxout.PdfRendererBuilder builder = new com.openhtmltopdf.pdfboxout.PdfRendererBuilder();
            // Pass the cleaned HTML to the builder
            builder.withHtmlContent(cleanedHtml, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        }
    }

    // HTML to DOCX conversion using Apache POI and Jsoup (simplified)
    private byte[] convertHtmlToDocx(String htmlContent) throws Exception {
        // Use Jsoup to parse and clean the HTML for DOCX conversion as well
        // This ensures consistent handling of entities and structure
        Document htmlDoc = Jsoup.parse(htmlContent);
        // For DOCX, you might want browser-like HTML output or just the text,
        // but Jsoup.parse() already cleans it up well.
        // You can convert named entities to numeric here too if needed, but POI might be more lenient.
        // htmlDoc.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml);
        // String cleanedHtml = htmlDoc.html(); // Use this if you need to pass processed HTML to the loop

        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            // Iterate over elements from the Jsoup parsed document's body
            for (Element element : htmlDoc.body().children()) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();

                // Basic handling for common tags
                if (element.tagName().equals("p")) {
                    run.setText(element.text());
                } else if (element.tagName().equals("h1")) {
                    run.setText(element.text());
                    run.setBold(true);
                    run.setFontSize(24);
                } else if (element.tagName().equals("h2")) {
                    run.setText(element.text());
                    run.setBold(true);
                    run.setFontSize(18);
                } else if (element.tagName().equals("h3")) {
                    run.setText(element.text());
                    run.setBold(true);
                    run.setFontSize(14);
                } else if (element.tagName().equals("strong") || element.tagName().equals("b")) {
                    run.setText(element.text());
                    run.setBold(true);
                } else if (element.tagName().equals("em") || element.tagName().equals("i")) {
                    run.setText(element.text());
                    run.setItalic(true);
                } else if (element.tagName().equals("ul") || element.tagName().equals("ol")) {
                    for (Element li : element.children()) {
                        XWPFParagraph listItemParagraph = document.createParagraph();
                        XWPFRun listItemRun = listItemParagraph.createRun();
                        if (element.tagName().equals("ul")) {
                            listItemRun.setText("• " + li.text());
                        } else {
                            listItemRun.setText(li.text());
                        }
                    }
                }
                else {
                    run.setText(element.text());
                }
            }

            document.write(bos);
            return bos.toByteArray();
        }
    }

    public byte[] getPdfDataById(Long id) {
        return ticketRepository.findById(id)
                .map(Ticket::getPdfData)
                .orElseThrow(() -> new TicketNotFoundException("PDF data not found for ticket id: " + id));
    }

    public byte[] getDocxDataById(Long id) {
        return ticketRepository.findById(id)
                .map(Ticket::getDocxData)
                .orElseThrow(() -> new TicketNotFoundException("DOCX data not found for ticket id: " + id));
    }

    public void deleteTicket(Long id) {
        try {
            if (!ticketRepository.existsById(id)) {
                throw new TicketNotFoundException(id);
            }
            ticketRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete ticket: " + e.getMessage(), e);
        }
    }
}