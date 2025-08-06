import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_URL_TICKETS = 'http://localhost:8080/api/tickets';
const BASE_URL_RELATIONSHIPS = 'http://localhost:8080/api/relationships';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  constructor(private http: HttpClient) {}

  // -------------------- Ticket CRUD --------------------
  getAllTickets(): Observable<any[]> {
    return this.http.get<any[]>(BASE_URL_TICKETS);
  }

  getTicketById(id: string): Observable<any> {
    return this.http.get<any>(`${BASE_URL_TICKETS}/${id}`);
  }

  createTicket(ticket: any): Observable<any> {
    return this.http.post(BASE_URL_TICKETS, ticket);
  }

  updateTicket(id: string, ticket: any): Observable<any> {
    return this.http.put(`${BASE_URL_TICKETS}/${id}`, ticket);
  }

  deleteTicket(id: number): Observable<any> {
    return this.http.delete(`${BASE_URL_TICKETS}/${id}`);
  }

  // -------------------- Description PATCH --------------------
  updateDescription(id: string, description: string): Observable<any> {
    return this.http.patch(`${BASE_URL_TICKETS}/${id}`, { description });
  }

  // -------------------- File Downloads --------------------
  downloadPdf(id: string): Observable<Blob> {
    return this.http.get(`${BASE_URL_TICKETS}/${id}/download-pdf`, { responseType: 'blob' });
  }

  downloadDocx(id: string): Observable<Blob> {
    return this.http.get(`${BASE_URL_TICKETS}/${id}/download-docx`, { responseType: 'blob' });
  }

  // -------------------- Subtasks --------------------
  getSubTickets(parentId: string): Observable<any[]> {
    return this.http.get<any[]>(`${BASE_URL_TICKETS}/${parentId}/subtickets`);
  }

  createSubTicket(parentId: string, subTicket: any): Observable<any> {
    return this.http.post(`${BASE_URL_TICKETS}/${parentId}/subtask`, subTicket);
  }

  // -------------------- Search (optional) --------------------
  searchTickets(query: string): Observable<any[]> {
    let params = new HttpParams().set('query', query);
    return this.http.get<any[]>(`${BASE_URL_RELATIONSHIPS}/search-tickets`, { params });
  }
}
