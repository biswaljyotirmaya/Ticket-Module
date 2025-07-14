// src/app/service/relationship.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_URL_RELATIONSHIPS = 'http://localhost:8080/api/relationships';

@Injectable({
  providedIn: 'root'
})
export class RelationshipService {
  constructor(private http: HttpClient) {}

  getRelationshipsBySourceTicket(sourceTicketId: string): Observable<any[]> {
    return this.http.get<any[]>(`${BASE_URL_RELATIONSHIPS}/by-source-ticket/${sourceTicketId}`);
  }

  createRelationship(relationship: any): Observable<any> {
    return this.http.post(BASE_URL_RELATIONSHIPS, relationship);
  }

  deleteRelationship(id: string): Observable<any> {
    return this.http.delete(`${BASE_URL_RELATIONSHIPS}/${id}`);
  }
}