// src/app/service/ticket.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_URL = 'http://localhost:8080/api/tickets';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  constructor(private http: HttpClient) {}

  getAllTickets(): Observable<any[]> {
    return this.http.get<any[]>(BASE_URL);
  }

  getTicketById(id: string): Observable<any> {
    return this.http.get<any>(`${BASE_URL}/${id}`);
  }

  createTicket(ticket: any): Observable<any> {
    return this.http.post(BASE_URL, ticket);
  }

  updateTicket(id: string, ticket: any): Observable<any> {
    return this.http.put(`${BASE_URL}/${id}`, ticket);
  }

  deleteTicket(id: string): Observable<any> {
    return this.http.delete(`${BASE_URL}/${id}`);
  }

  // ✅ New PATCH method for updating description only
  updateDescription(id: string, description: string): Observable<any> {
    return this.http.patch(`${BASE_URL}/${id}`, { subTask: description });
  }
}
