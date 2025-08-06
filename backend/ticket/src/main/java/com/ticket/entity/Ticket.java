package com.ticket.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(generator = "shared-seq")
    @GenericGenerator(name = "shared-seq", strategy = "com.ticket.generator.SharedSequenceGenerator")
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String titleTicket;

    @NotBlank(message = "Assignee is required")
    @Column(nullable = false)
    private String assign;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status;

    @NotBlank(message = "Priority is required")
    @Column(nullable = false)
    private String priority;

    @Column(nullable = false)
    private LocalDate dateCurrent;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Lob
    @Column(name = "pdf_data", columnDefinition="LONGBLOB")
    private byte[] pdfData;

    @Lob
    @Column(name = "docx_data", columnDefinition="LONGBLOB")
    private byte[] docxData;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "parent_ticket_id")
    private Ticket parentTicket;


    @OneToMany(mappedBy = "parentTicket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ticket> subTickets = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (dateCurrent == null) {
            dateCurrent = LocalDate.now();
        }
    }
}
