import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TicketService } from '../service/ticket.service';

@Component({
  selector: 'app-ticket-panel',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="container py-4" *ngIf="ticket">
      <div class="card shadow-sm mb-4">
        <div class="card-body">
          <div class="d-flex align-items-center mb-3">
            <span class="badge bg-secondary me-2">Task</span>
            <span class="text-muted small">{{ ticket.id }}</span>
            <button class="btn btn-outline-secondary btn-sm ms-auto">Ask AI</button>
          </div>
          <h4 class="card-title mb-3">{{ ticket.title }}</h4>
          <div class="alert alert-info py-2 px-3 mb-4" *ngIf="ticket.info">
            <span class="text-primary">Ask Brain to improve the description · find similar tasks · or ask about this task</span>
          </div>
          <div class="row mb-3">
            <div class="col-md-3 mb-2">
              <div class="text-muted">Status</div>
              <button class="btn btn-outline-secondary btn-sm">{{ ticket.status || 'Mark Complete' }}</button>
            </div>
            <div class="col-md-3 mb-2">
              <div class="text-muted">Assignees</div>
              <span>{{ ticket.assignee || 'Empty' }}</span>
            </div>
            <div class="col-md-3 mb-2">
              <div class="text-muted">Priority</div>
              <span>{{ ticket.priority || 'Empty' }}</span>
            </div>
            <div class="col-md-3 mb-2">
              <div class="text-muted">Tags</div>
              <span>{{ ticket.tags || 'Empty' }}</span>
            </div>
          </div>
          <div class="mb-2">
            <div class="text-muted">Description</div>
            <p class="card-text">{{ ticket.description }}</p>
          </div>
        </div>
      </div>
    </div>
  `
})
export class TicketPanelComponent implements OnInit {
  ticket: any = null;

  constructor(
    private route: ActivatedRoute,
    private ticketService: TicketService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.ticketService.getTicketById(id).subscribe({
        next: data => this.ticket = data,
        error: err => alert('Error fetching ticket')
      });
    }
  }
}
