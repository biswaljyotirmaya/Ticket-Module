package com.ticket.mapper;

import com.ticket.dto.LinkDTO;
import com.ticket.entity.Link;

public class LinkMapper {

    public static LinkDTO toDTO(Link link) {
        if (link == null) return null;

        LinkDTO dto = new LinkDTO();
        dto.setId(link.getId());
        dto.setUrl(link.getUrl());
        dto.setDescription(link.getDescription());

        if (link.getParentTicket() != null) {
            dto.setParentTicketId(link.getParentTicket().getId());
        }
        return dto;
    }

    public static Link toEntity(LinkDTO dto) {
        if (dto == null) return null;

        Link link = new Link();
        link.setId(dto.getId());
        link.setUrl(dto.getUrl());
        link.setDescription(dto.getDescription());
        return link;
    }
}
