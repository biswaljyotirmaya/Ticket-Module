import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TicketService } from '../service/ticket.service';

declare var bootstrap: any;

@Component({
  selector: 'app-ticket-crud',
  templateUrl: './ticket-crud.component.html',
  styleUrls: ['./ticket-crud.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class TicketCrudComponent implements OnInit {
  tickets: any[] = [];
  pagedTickets: any[] = [];
  currentPage = 1;
  pageSize = 13;

  ticket = {
    sno: '',
    titleTicket: '',
    assign: '',
    status: '',
    priority: '',
    dateCurrent: '',
    subTask: ''
  };

  deleteSno: string = '';

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private ticketService = inject(TicketService);

  ngOnInit() {
    this.getTickets();
  }

  getTickets() {
    this.ticketService.getAllTickets().subscribe({
      next: data => {
        this.tickets = data;
        this.updatePagedTickets();
      },
      error: err => alert('Error fetching tickets')
    });
  }

  updatePagedTickets() {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.pagedTickets = this.tickets.slice(start, end);
  }

  changePage(page: number) {
    if (page >= 1 && page <= Math.ceil(this.tickets.length / this.pageSize)) {
      this.currentPage = page;
      this.updatePagedTickets();
    }
  }

  createTicket() {
    this.ticketService.createTicket(this.ticket).subscribe({
      next: () => {
        this.getTickets();
        this.resetForm();
        alert('Ticket created successfully!');
      },
      error: err => alert('Error creating ticket')
    });
  }

  openEditModal(ticket: any) {
    this.ticket = { ...ticket };
    const modalEl = document.getElementById('editTicketModal');
    if (modalEl) {
      const modal = new bootstrap.Modal(modalEl);
      modal.show();
    }
  }

  updateTicket() {
    this.ticketService.updateTicket(this.ticket.sno, this.ticket).subscribe({
      next: () => {
        this.getTickets();
        this.resetForm();
        alert('Ticket updated successfully!');
      },
      error: err => alert('Error updating ticket')
    });
  }

  confirmDelete(sno: string) {
    this.deleteSno = sno;
    const modalEl = document.getElementById('deleteConfirmModal');
    if (modalEl) {
      const modal = new bootstrap.Modal(modalEl);
      modal.show();
    }
  }

  deleteTicket() {
    this.ticketService.deleteTicket(this.deleteSno).subscribe({
      next: () => {
        this.getTickets();
        alert('Ticket deleted successfully!');
      },
      error: err => alert('Error deleting ticket')
    });
  }

  resetForm() {
    this.ticket = {
      sno: '',
      titleTicket: '',
      assign: '',
      status: '',
      priority: '',
      dateCurrent: '',
      subTask: ''
    };
  }

  goToTicketPanel(ticketId: string) {
    this.router.navigate(['/ticket', ticketId]);
  }
}
