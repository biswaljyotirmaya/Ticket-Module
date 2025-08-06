import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { QuillModule } from 'ngx-quill';

import { TicketService } from '../service/ticket.service';
import { LinkService } from '../service/link.service';
import { RelationshipService } from '../service/relationship.service';

declare var bootstrap: any;

interface TicketRef {
  id: number;
}

@Component({
  selector: 'app-ticket-panel',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, QuillModule],
  templateUrl: './ticket-panel.component.html',
  styleUrls: ['./ticket-panel.component.css'],
})
export class TicketPanelComponent implements OnInit {
  ticket: any = null;
  saving = false;
  private descriptionTimer: any;

  private route = inject(ActivatedRoute);
  private ticketService = inject(TicketService);
  private linkService = inject(LinkService);
  private relationshipService = inject(RelationshipService);
  // Inject router properly here:
  private router = inject(Router);

  subTickets: any[] = [];
  newSubTask = this.initSubTask();
  editingSubTask: any = {};
  deleteSubTicketId = '';

  links: any[] = [];
  newLink = this.initLink();
  deleteLinkId = '';

  relationships: any[] = [];
  newRelationship = this.initRelationship();
  relationshipTypes = ['Blocking', 'Waiting On', 'Related To'];
  searchTargetTicketQuery = '';
  searchTicketResults: any[] = [];
  selectedTargetTicket: any = null;
  deleteRelationshipId = '';

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (!id) return;

      this.ticketService.getTicketById(id).subscribe({
        next: (data) => {
          this.ticket = {
            id: data.id,
            titleTicket: data.titleTicket,
            assign: data.assign,
            status: data.status,
            priority: data.priority,
            dateCurrent: data.dateCurrent,
            description: data.description || '',
          };

          this.newSubTask.parentTicket = { id: this.ticket.id };
          this.newLink.parentTicket = { id: this.ticket.id };
          this.newRelationship.sourceTicket = { id: this.ticket.id };

          this.loadSubTickets(id);
          this.loadLinks(id);
          this.loadRelationships(id);
        },
        error: (err) => {
          alert('Error fetching ticket');
          console.error(err);
        },
      });
    });
  }

  // Description editor handlers
  onContentChanged(event: any): void {
    this.ticket.description = event.html;
    clearTimeout(this.descriptionTimer);
    this.descriptionTimer = setTimeout(() => this.autoSaveDescription(), 2000);
  }

  autoSaveDescription(): void {
    if (!this.ticket?.id) return;
    this.saving = true;
    this.ticketService.updateDescription(this.ticket.id, this.ticket.description).subscribe({
      next: () => (this.saving = false),
      error: (err) => {
        this.saving = false;
        alert('Error saving description');
        console.error(err);
      },
    });
  }

  // Download ticket files
  downloadPdf(): void {
    if (!this.ticket?.id) return;
    this.ticketService.downloadPdf(this.ticket.id).subscribe({
      next: (data: Blob) => this.triggerDownload(data, 'pdf'),
      error: (err) => {
        alert('Error downloading PDF.');
        console.error(err);
      },
    });
  }

  downloadDocx(): void {
    if (!this.ticket?.id) return;
    this.ticketService.downloadDocx(this.ticket.id).subscribe({
      next: (data: Blob) => this.triggerDownload(data, 'docx'),
      error: (err) => {
        alert('Error downloading DOCX.');
        console.error(err);
      },
    });
  }

  private triggerDownload(data: Blob, ext: string): void {
    const blob = new Blob([data]);
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `ticket_${this.ticket.id}.${ext}`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }

  // Subtasks
  loadSubTickets(ticketId: string): void {
    this.ticketService.getSubTickets(ticketId).subscribe({
      next: (data) => (this.subTickets = data),
      error: (err) => {
        alert('Error fetching subtasks');
        console.error(err);
      },
    });
  }

  createSubTicket(): void {
    const subTicketToCreate = { ...this.newSubTask, parentTicket: { id: this.ticket.id } };
    this.ticketService.createSubTicket(this.ticket.id, subTicketToCreate).subscribe({
      next: () => {
        alert('Subtask created successfully!');
        this.loadSubTickets(this.ticket.id);
        this.newSubTask = this.initSubTask();
        this.newSubTask.parentTicket = { id: this.ticket.id };
      },
      error: (err) => {
        alert('Error creating subtask');
        console.error(err);
      },
    });
  }

  openEditSubTicketModal(subTicket: any): void {
    this.editingSubTask = { ...subTicket };
    if (this.editingSubTask.dateCurrent) {
      this.editingSubTask.dateCurrent = new Date(this.editingSubTask.dateCurrent).toISOString().substring(0, 10);
    }
    const modalEl = document.getElementById('editSubTicketModal');
    if (modalEl) new bootstrap.Modal(modalEl).show();
  }

  updateSubTicket(): void {
    this.ticketService.updateTicket(this.editingSubTask.id, this.editingSubTask).subscribe({
      next: () => {
        alert('Subtask updated successfully!');
        this.loadSubTickets(this.ticket.id);
        this.editingSubTask = {};
      },
      error: (err) => {
        alert('Error updating subtask');
        console.error(err);
      },
    });
  }

  confirmDeleteSubTicket(id: string): void {
    this.deleteSubTicketId = id;
    const modalEl = document.getElementById('deleteSubTicketConfirmModal');
    if (modalEl) new bootstrap.Modal(modalEl).show();
  }

  deleteSubTicket(): void {
    this.ticketService.deleteTicket(Number(this.deleteSubTicketId)).subscribe({
      next: () => {
        alert('Subtask deleted successfully!');
        this.loadSubTickets(this.ticket.id);
        this.deleteSubTicketId = '';
      },
      error: (err) => {
        alert('Error deleting subtask');
        console.error(err);
      },
    });
  }

  private initSubTask() {
    return {
      titleTicket: '',
      assign: '',
      status: '',
      priority: '',
      description: '',
      dateCurrent: new Date().toISOString().substring(0, 10),
      parentTicket: null as TicketRef | null,
    };
  }

  // Links
  private initLink() {
    return {
      url: '',
      description: '',
      parentTicket: null as TicketRef | null,
    };
  }

  loadLinks(ticketId: string): void {
    this.linkService.getLinksByParentTicket(ticketId).subscribe({
      next: (data) => (this.links = data),
      error: (err) => {
        alert('Error fetching links');
        console.error(err);
      },
    });
  }

  addLink(): void {
    if (!this.newLink.url.startsWith('http://') && !this.newLink.url.startsWith('https://')) {
      alert('Please enter a valid URL (must start with http:// or https://)');
      return;
    }

    const linkToCreate = {
      ...this.newLink,
      parentTicket: { id: this.ticket.id },
    };

    this.linkService.createLink(linkToCreate).subscribe({
      next: () => {
        alert('Link added successfully!');
        this.loadLinks(this.ticket.id);
        this.newLink = this.initLink();
        this.newLink.parentTicket = { id: this.ticket.id };
      },
      error: (err) => {
        alert('Error adding link');
        console.error(err);
      },
    });
  }

  confirmDeleteLink(linkId: string): void {
    this.deleteLinkId = linkId;
    const modalEl = document.getElementById('deleteLinkConfirmModal');
    if (modalEl) new bootstrap.Modal(modalEl).show();
  }

  deleteLink(): void {
    this.linkService.deleteLink(this.deleteLinkId).subscribe({
      next: () => {
        alert('Link deleted successfully!');
        this.loadLinks(this.ticket.id);
        this.deleteLinkId = '';
      },
      error: (err) => {
        alert('Error deleting link');
        console.error(err);
      },
    });
  }

  // Relationships
  private initRelationship() {
    return {
      type: '',
      targetTicket: null as TicketRef | null,
      sourceTicket: null as TicketRef | null,
    };
  }

  loadRelationships(ticketId: string): void {
    this.relationshipService.getRelationshipsBySourceTicket(ticketId).subscribe({
      next: (data) => (this.relationships = data),
      error: (err) => {
        alert('Error fetching relationships');
        console.error(err);
      },
    });
  }

  searchTickets(): void {
    if (this.searchTargetTicketQuery.length > 2) {
      this.ticketService.searchTickets(this.searchTargetTicketQuery).subscribe({
        next: (data) => (this.searchTicketResults = data),
        error: (err) => console.error('Error searching tickets', err),
      });
    } else {
      this.searchTicketResults = [];
    }
  }

  selectTargetTicket(ticket: any): void {
    this.selectedTargetTicket = ticket;
    this.searchTargetTicketQuery = ticket.title;
    this.searchTicketResults = [];
  }

  addRelationship(): void {
    if (!this.newRelationship.type || !this.selectedTargetTicket) {
      alert('Please select a relationship type and a target ticket.');
      return;
    }

    const relationshipToCreate = {
      type: this.newRelationship.type,
      sourceTicket: { id: this.ticket.id },
      targetTicketId: this.selectedTargetTicket.id, // backend expects targetTicketId
    };

    this.relationshipService.createRelationship(relationshipToCreate).subscribe({
      next: () => {
        alert('Relationship added successfully!');
        this.loadRelationships(this.ticket.id);
        this.resetNewRelationshipForm();
      },
      error: (err) => {
        alert('Error adding relationship');
        console.error(err);
      },
    });
  }

  confirmDeleteRelationship(relationshipId: string): void {
    this.deleteRelationshipId = relationshipId;
    const modalEl = document.getElementById('deleteRelationshipConfirmModal');
    if (modalEl) new bootstrap.Modal(modalEl).show();
  }

  deleteRelationship(): void {
    this.relationshipService.deleteRelationship(this.deleteRelationshipId).subscribe({
      next: () => {
        alert('Relationship deleted successfully!');
        this.loadRelationships(this.ticket.id);
        this.deleteRelationshipId = '';
      },
      error: (err) => {
        alert('Error deleting relationship');
        console.error(err);
      },
    });
  }

  private resetNewRelationshipForm(): void {
    this.newRelationship = this.initRelationship();
    this.newRelationship.sourceTicket = { id: this.ticket.id };
    this.searchTargetTicketQuery = '';
    this.selectedTargetTicket = null;
    this.searchTicketResults = [];
  }

  goToTicketPanel(ticketId: number): void {
    if (!ticketId) return;
    this.router.navigate(['/ticket', ticketId]);
  }
}
