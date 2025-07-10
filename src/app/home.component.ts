import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="d-flex flex-column align-items-center justify-content-center vh-100">
      <div class="d-flex gap-3">
        <a [routerLink]="['/ticket']" [queryParams]="{ action: 'create' }" class="btn btn-primary btn-lg">Create Ticket</a>
        <a [routerLink]="['/ticket']" [queryParams]="{ action: 'get' }" class="btn btn-secondary btn-lg">Get Ticket</a>
      </div>
    </div>
  `
})
export class HomeComponent {} 