package com.ticket.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticket.dto.TicketDTO;
import com.ticket.entity.Ticket;
import com.ticket.exception.TicketNotFoundException;
import com.ticket.mapper.TicketMapper;
import com.ticket.repository.TicketRepository;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public TicketDTO saveTicket(TicketDTO dto) {
        Ticket ticket = TicketMapper.toEntity(dto);
        Ticket saved = ticketRepository.save(ticket);
        return TicketMapper.toDTO(saved);
    }

    public TicketDTO updateTicket(Long id, TicketDTO dto) {
        return ticketRepository.findById(id)
                .map(existing -> {
                    Ticket updated = TicketMapper.toEntity(dto);
                    updated.setId(id);
                    Ticket saved = ticketRepository.save(updated);
                    return TicketMapper.toDTO(saved);
                }).orElseThrow(() -> new TicketNotFoundException(id));
    }

    public TicketDTO updateTicketDescription(Long id, String description) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setDescription(description);
                    try {
                        ticket.setPdfData(convertHtmlToPdf(description));
                        ticket.setDocxData(convertHtmlToDocx(description));
                    } catch (Exception e) {
                        ticket.setPdfData(null);
                        ticket.setDocxData(null);
                    }
                    Ticket saved = ticketRepository.save(ticket);
                    return TicketMapper.toDTO(saved);
                }).orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<TicketDTO> getRootTickets() {
        return ticketRepository.findByParentTicketIsNull()
                .stream()
                .map(TicketMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TicketDTO> getSubTicketsByParent(Long parentId) {
        Ticket parent = ticketRepository.findById(parentId)
                .orElseThrow(() -> new TicketNotFoundException(parentId));
        return ticketRepository.findByParentTicket(parent)
                .stream()
                .map(TicketMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<TicketDTO> getTicketById(Long id) {
        return ticketRepository.findById(id).map(TicketMapper::toDTO);
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
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

    private byte[] convertHtmlToPdf(String htmlContent) throws Exception {
        Document doc = Jsoup.parse(htmlContent);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        doc.outputSettings().charset("UTF-8");
        doc.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml);
        String cleanedHtml = doc.html();

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            new com.openhtmltopdf.pdfboxout.PdfRendererBuilder()
                    .withHtmlContent(cleanedHtml, null)
                    .toStream(os)
                    .run();
            return os.toByteArray();
        }
    }

    private byte[] convertHtmlToDocx(String htmlContent) throws Exception {
        Document htmlDoc = Jsoup.parse(htmlContent);
        try (XWPFDocument document = new XWPFDocument(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            for (Element element : htmlDoc.body().children()) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                String text = element.text();
                switch (element.tagName()) {
                    case "h1": run.setBold(true); run.setFontSize(24); break;
                    case "h2": run.setBold(true); run.setFontSize(18); break;
                    case "h3": run.setBold(true); run.setFontSize(14); break;
                    case "strong": case "b": run.setBold(true); break;
                    case "em": case "i": run.setItalic(true); break;
                    case "ul": case "ol":
                        for (Element li : element.children()) {
                            XWPFParagraph listParagraph = document.createParagraph();
                            XWPFRun listRun = listParagraph.createRun();
                            listRun.setText("ul".equals(element.tagName()) ? "• " + li.text() : li.text());
                        }
                        continue;
                }
                run.setText(text);
            }
            document.write(bos);
            return bos.toByteArray();
        }
    }
}
