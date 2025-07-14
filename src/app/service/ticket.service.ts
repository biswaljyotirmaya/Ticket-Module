// // src/app/service/ticket.service.ts
// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';

// const BASE_URL = 'http://localhost:8080/api/tickets';

// @Injectable({
//   providedIn: 'root'
// })
// export class TicketService {
//   constructor(private http: HttpClient) {}

//   getAllTickets(): Observable<any[]> {
//     return this.http.get<any[]>(BASE_URL);
//   }

//   getTicketById(id: string): Observable<any> {
//     return this.http.get<any>(`${BASE_URL}/${id}`);
//   }

//   createTicket(ticket: any): Observable<any> {
//     return this.http.post(BASE_URL, ticket);
//   }

//   updateTicket(id: string, ticket: any): Observable<any> {
//     return this.http.put(`${BASE_URL}/${id}`, ticket);
//   }

//   deleteTicket(id: string): Observable<any> {
//     return this.http.delete(`${BASE_URL}/${id}`);
//   }

//   // ✅ New PATCH method for updating description only
//   updateDescription(id: string, description: string): Observable<any> {
//     return this.http.patch(`${BASE_URL}/${id}`, { subTask: description });
//   }
// }

// src/app/service/ticket.service.ts
// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';

// const BASE_URL_TICKETS = 'http://localhost:8080/api/tickets';
// const BASE_URL_SUBTICKETS = 'http://localhost:8080/api/subtickets';

// @Injectable({
//   providedIn: 'root',
// })
// export class TicketService {
//   constructor(private http: HttpClient) {}

//   // Ticket API calls
//   getAllTickets(): Observable<any[]> {
//     return this.http.get<any[]>(BASE_URL_TICKETS);
//   }

//   getTicketById(id: string): Observable<any> {
//     return this.http.get<any>(`${BASE_URL_TICKETS}/${id}`);
//   }

//   createTicket(ticket: any): Observable<any> {
//     return this.http.post(BASE_URL_TICKETS, ticket);
//   }

//   updateTicket(id: string, ticket: any): Observable<any> {
//     return this.http.put(`${BASE_URL_TICKETS}/${id}`, ticket);
//   }

//   deleteTicket(id: string): Observable<any> {
//     return this.http.delete(`${BASE_URL_TICKETS}/${id}`);
//   }

//   // PATCH method for updating description only
//   updateDescription(id: string, description: string): Observable<any> {
//     // Send a PATCH request with the 'description' field
//     return this.http.patch(`${BASE_URL_TICKETS}/${id}`, {
//       description: description,
//     });
//   }

//   // SubTicket API calls
//   getAllSubTicketsForTicket(ticketId: string): Observable<any[]> {
//     return this.http.get<any[]>(`${BASE_URL_SUBTICKETS}/by-ticket/${ticketId}`);
//   }

//   createSubTicket(subTicket: any): Observable<any> {
//     return this.http.post(BASE_URL_SUBTICKETS, subTicket);
//   }

//   updateSubTicket(id: string, subTicket: any): Observable<any> {
//     return this.http.put(`${BASE_URL_SUBTICKETS}/${id}`, subTicket);
//   }

//   deleteSubTicket(id: string): Observable<any> {
//     return this.http.delete(`${BASE_URL_SUBTICKETS}/${id}`);
//   }
// }





// // src/app/service/ticket.service.ts
// import { Injectable } from '@angular/core';
// import { HttpClient, HttpParams } from '@angular/common/http'; // Import HttpParams
// import { Observable } from 'rxjs';

// const BASE_URL_TICKETS = 'http://localhost:8080/api/tickets';
// const BASE_URL_SUBTICKETS = 'http://localhost:8080/api/subtickets';
// const BASE_URL_RELATIONSHIPS = 'http://localhost:8080/api/relationships'; // Add this

// @Injectable({
//   providedIn: 'root',
// })
// export class TicketService {
//   constructor(private http: HttpClient) {}

//   // Ticket API calls
//   getAllTickets(): Observable<any[]> {
//     return this.http.get<any[]>(BASE_URL_TICKETS);
//   }

//   getTicketById(id: string): Observable<any> {
//     return this.http.get<any>(`${BASE_URL_TICKETS}/${id}`);
//   }

//   createTicket(ticket: any): Observable<any> {
//     return this.http.post(BASE_URL_TICKETS, ticket);
//   }

//   updateTicket(id: string, ticket: any): Observable<any> {
//     return this.http.put(`${BASE_URL_TICKETS}/${id}`, ticket);
//   }

//   deleteTicket(id: string): Observable<any> {
//     return this.http.delete(`${BASE_URL_TICKETS}/${id}`);
//   }

//   // PATCH method for updating description only
//   updateDescription(id: string, description: string): Observable<any> {
//     return this.http.patch(`${BASE_URL_TICKETS}/${id}`, {
//       description: description,
//     });
//   }

//   // New method to search tickets for relationships
//   searchTickets(query: string): Observable<any[]> {
//     let params = new HttpParams().set('query', query);
//     return this.http.get<any[]>(`${BASE_URL_RELATIONSHIPS}/search-tickets`, {
//       params,
//     });
//   }

//   // SubTicket API calls
//   getAllSubTicketsForTicket(ticketId: string): Observable<any[]> {
//     return this.http.get<any[]>(`${BASE_URL_SUBTICKETS}/by-ticket/${ticketId}`);
//   }

//   createSubTicket(subTicket: any): Observable<any> {
//     return this.http.post(BASE_URL_SUBTICKETS, subTicket);
//   }

//   updateSubTicket(id: string, subTicket: any): Observable<any> {
//     return this.http.put(`${BASE_URL_SUBTICKETS}/${id}`, subTicket);
//   }

//   deleteSubTicket(id: string): Observable<any> {
//     return this.http.delete(`${BASE_URL_SUBTICKETS}/${id}`);
//   }
// }








//===================================
// src/app/service/ticket.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_URL_TICKETS = 'http://localhost:8080/api/tickets';
const BASE_URL_SUBTICKETS = 'http://localhost:8080/api/subtickets';
const BASE_URL_LINKS = 'http://localhost:8080/api/links'; // Ensure this is present
const BASE_URL_RELATIONSHIPS = 'http://localhost:8080/api/relationships'; // Ensure this is present

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  constructor(private http: HttpClient) {}

  // Ticket API calls
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

  deleteTicket(id: string): Observable<any> {
    return this.http.delete(`${BASE_URL_TICKETS}/${id}`);
  }

  // PATCH method for updating description only (triggers PDF/DOCX conversion on backend)
  updateDescription(id: string, description: string): Observable<any> {
    return this.http.patch(`${BASE_URL_TICKETS}/${id}`, { description: description });
  }

  // New method to download PDF
  downloadPdf(id: string): Observable<Blob> {
    return this.http.get(`${BASE_URL_TICKETS}/${id}/download-pdf`, { responseType: 'blob' });
  }

  // New method to download DOCX
  downloadDocx(id: string): Observable<Blob> {
    return this.http.get(`${BASE_URL_TICKETS}/${id}/download-docx`, { responseType: 'blob' });
  }

  // New method to search tickets for relationships
  searchTickets(query: string): Observable<any[]> {
    let params = new HttpParams().set('query', query);
    return this.http.get<any[]>(`${BASE_URL_RELATIONSHIPS}/search-tickets`, { params });
  }

  // SubTicket API calls (unchanged)
  getAllSubTicketsForTicket(ticketId: string): Observable<any[]> {
    return this.http.get<any[]>(`${BASE_URL_SUBTICKETS}/by-ticket/${ticketId}`);
  }

  createSubTicket(subTicket: any): Observable<any> {
    return this.http.post(BASE_URL_SUBTICKETS, subTicket);
  }

  updateSubTicket(id: string, subTicket: any): Observable<any> {
    return this.http.put(`${BASE_URL_SUBTICKETS}/${id}`, subTicket);
  }

  deleteSubTicket(id: string): Observable<any> {
    return this.http.delete(`${BASE_URL_SUBTICKETS}/${id}`);
  }
}