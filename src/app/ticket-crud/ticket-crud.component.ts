import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TicketService } from '../service/ticket.service';

declare var bootstrap: any;

interface Ticket {
  id?: number; // optional for create
  titleTicket: string;
  assign: string;
  status: string;
  priority: string;
  dateCurrent: string;
  description: string;
}

@Component({
  selector: 'app-ticket-crud',
  templateUrl: './ticket-crud.component.html',
  styleUrls: ['./ticket-crud.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule],
})
export class TicketCrudComponent implements OnInit {
  tickets: Ticket[] = [];

  ticket: Ticket = {
  titleTicket: '',
  assign: '',
  status: '',
  priority: '',
  dateCurrent: '',
  description: '',
};


  deleteId: number | null = null;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private ticketService = inject(TicketService);

  ngOnInit() {
    this.getTickets();
  }

  getTickets() {
    this.ticketService.getAllTickets().subscribe({
      next: (data) => {
        this.tickets = data;
      },
      error: () => alert('Error fetching tickets'),
    });
  }

  createTicket() {
    this.ticketService.createTicket(this.ticket).subscribe({
      next: () => {
        this.getTickets();
        this.resetForm();
        alert('Ticket created successfully!');
      },
      error: () => alert('Error creating ticket'),
    });
  }

  openEditModal(ticket: Ticket) {
    this.ticket = { ...ticket };
    // Format date for date input YYYY-MM-DD
    if (this.ticket.dateCurrent) {
      this.ticket.dateCurrent = new Date(this.ticket.dateCurrent).toISOString().substring(0, 10);
    }
    const modalEl = document.getElementById('editTicketModal');
    if (modalEl) {
      const modal = new bootstrap.Modal(modalEl);
      modal.show();
    }
  }

  updateTicket() {
    if (!this.ticket.id) {
      alert('Ticket ID missing!');
      return;
    }
    this.ticketService.updateTicket(this.ticket.id.toString(), this.ticket).subscribe({
      next: () => {
        this.getTickets();
        this.resetForm();
        alert('Ticket updated successfully!');
      },
      error: () => alert('Error updating ticket'),
    });
  }

  confirmDelete(id: number) {
    this.deleteId = id;
    const modalEl = document.getElementById('deleteConfirmModal');
    if (modalEl) {
      const modal = new bootstrap.Modal(modalEl);
      modal.show();
    }
  }

  deleteTicket() {
    if (this.deleteId === null) return;
    this.ticketService.deleteTicket(this.deleteId).subscribe({
      next: () => {
        this.getTickets();
        alert('Ticket deleted successfully!');
      },
      error: () => alert('Error deleting ticket'),
    });
  }

  resetForm() {
    this.ticket = {
      titleTicket: '',
      assign: '',
      status: '',
      priority: '',
      dateCurrent: '',
      description: '',
    };
  }

  goToTicketPanel(ticketId: number) {
    this.router.navigate(['/ticket', ticketId]);
  }
}
