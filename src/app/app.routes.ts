// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { TicketCrudComponent } from './ticket-crud/ticket-crud.component';
import { HomeComponent } from './home.component';
import { TicketPanelComponent } from './ticket-panel/ticket-panel.component';

export const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'ticket', component: TicketCrudComponent },
  { path: 'ticket/:id', component: TicketPanelComponent }
];
