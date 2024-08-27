import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeoutDialogComponent } from './timeout-dialog.component';

describe('TimeoutDialogComponent', () => {
  let component: TimeoutDialogComponent;
  let fixture: ComponentFixture<TimeoutDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TimeoutDialogComponent],
    });
    fixture = TestBed.createComponent(TimeoutDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
