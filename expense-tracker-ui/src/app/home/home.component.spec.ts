import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';

import { ExpenseService } from '../services/expense.service';
import { HomeComponent } from './home.component';

class MockExpenseService {
  getExpense() {
    return of([]);
  }

  addExpense() {
    return of({ id: 1, title: 'Test', amount: 10, category: 'Food' });
  }

  deleteExpense() {
    return of({});
  }

  getDbStatus() {
    return of('Connected to PostgreSQL pod: postgres-0');
  }
}

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomeComponent],
      imports: [FormsModule],
      providers: [{ provide: ExpenseService, useClass: MockExpenseService }],
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render postgres connection message when value is present', () => {
    component.dbConnectionMessage = 'Connected to PostgreSQL pod: postgres-0';
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain(
      'Connected to PostgreSQL pod: postgres-0',
    );
  });
});
