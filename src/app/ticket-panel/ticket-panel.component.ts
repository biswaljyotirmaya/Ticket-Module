// src/app/ticket-panel/ticket-panel.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TicketService } from '../service/ticket.service';

@Component({
  selector: 'app-ticket-panel',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ticket-panel.component.html',
  styleUrls: ['./ticket-panel.component.css']
})
export class TicketPanelComponent implements OnInit {
  ticket: any = null;

  constructor(
    private route: ActivatedRoute,
    private ticketService: TicketService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    console.log('Ticket ID from route:', id);

    if (id) {
      this.ticketService.getTicketById(id).subscribe({
        next: data => {
          console.log('Ticket fetched:', data);
          // Map backend field names to frontend
          this.ticket = {
            id: data.sno,
            title: data.titleTicket,
            status: data.status,
            assignee: data.assign,
            priority: data.priority,
            tags: data.tags || '', // optional
            description: data.subTask
          };
        },
        error: err => {
          console.error('Error fetching ticket:', err);
          alert('Error fetching ticket');
        }
      });
    }
  }
}
