package com.ticket.dto;

import lombok.Data;

@Data
public class LinkDTO {
    private Long id;
    private String url;
    private String description;
    private Long parentTicketId;
}
