// src/app/service/link.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_URL_LINKS = 'http://localhost:8080/api/links';

@Injectable({
  providedIn: 'root'
})
export class LinkService {
  constructor(private http: HttpClient) {}

  getLinksByParentTicket(ticketId: string): Observable<any[]> {
    return this.http.get<any[]>(`${BASE_URL_LINKS}/by-ticket/${ticketId}`);
  }

  createLink(link: any): Observable<any> {
    return this.http.post(BASE_URL_LINKS, link);
  }

  deleteLink(id: string): Observable<any> {
    return this.http.delete(`${BASE_URL_LINKS}/${id}`);
  }
}