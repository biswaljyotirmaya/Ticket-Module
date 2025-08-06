package com.ticket.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class TicketDTO {

	private Long id;
	private String titleTicket;
	private String assign;
	private String status;
	private String priority;
	private LocalDate dateCurrent;
	private String description;
	private byte[] pdfData;
	private byte[] docxData;
	private Long parentTicketId;
	private List<TicketDTO> subTicketIds;

}
