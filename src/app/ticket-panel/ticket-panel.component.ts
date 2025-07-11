import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; // ✅ Import FormsModule
import { TicketService } from '../service/ticket.service';

@Component({
  selector: 'app-ticket-panel',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule], // ✅ Add it here
  templateUrl: './ticket-panel.component.html',
  styleUrls: ['./ticket-panel.component.css']
})
export class TicketPanelComponent implements OnInit {
  ticket: any = null;
  saving: boolean = false;
  private descriptionTimer: any;

  constructor(
    private route: ActivatedRoute,
    private ticketService: TicketService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.ticketService.getTicketById(id).subscribe({
        next: data => {
          this.ticket = {
            id: data.sno,
            title: data.titleTicket,
            status: data.status,
            assignee: data.assign,
            priority: data.priority,
            tags: data.tags || '',
            description: data.subTask,
            date:data.dateCurrent
          };
        },
        error: err => {
          alert('Error fetching ticket');
        }
      });
    }
  }

  onDescriptionChange(): void {
    clearTimeout(this.descriptionTimer);
    this.descriptionTimer = setTimeout(() => {
      this.autoSaveDescription();
    }, 2000);
  }

  autoSaveDescription(): void {
    if (!this.ticket?.id) return;
    this.saving = true;

    this.ticketService.updateDescription(this.ticket.id, this.ticket.description).subscribe({
      next: () => {
        this.saving = false;
      },
      error: () => {
        this.saving = false;
      }
    });
  }
}
