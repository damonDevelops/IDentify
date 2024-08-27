import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RedirectDialogComponent } from './redirect-dialog.component';

describe('RedirectDialogComponent', () => {
  let component: RedirectDialogComponent;
  let fixture: ComponentFixture<RedirectDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RedirectDialogComponent],
    });
    fixture = TestBed.createComponent(RedirectDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
