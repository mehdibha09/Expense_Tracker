import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Expense } from '../shared/models/expense.model';

@Injectable({
  providedIn: 'root',
})
export class ExpenseService {
  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  getExpense() {
    return this.http.get<Expense[]>(`${this.baseUrl}/show`);
  }

  addExpense(expense: Expense): Observable<Expense> {
    return this.http.post<Expense>(`${this.baseUrl}/add`, expense);
  }

  deleteExpense(id: number) {
    return this.http.delete(`${this.baseUrl}/delete`, {
      params: { id: id.toString() },
      responseType: 'text',
    });
  }

  getDbStatus(): Observable<string> {
    return this.http.get(`${this.baseUrl}/db-status`, { responseType: 'text' });
  }
}
