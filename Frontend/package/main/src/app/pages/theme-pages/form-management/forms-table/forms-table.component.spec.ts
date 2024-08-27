import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormsTableComponent } from './forms-table.component';

describe('FormsTableComponent', () => {
  let component: FormsTableComponent;
  let fixture: ComponentFixture<FormsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormsTableComponent],
    });
    fixture = TestBed.createComponent(FormsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
