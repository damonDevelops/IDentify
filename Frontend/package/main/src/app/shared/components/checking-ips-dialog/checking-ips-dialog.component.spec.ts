import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckingIpsDialogComponent } from './checking-ips-dialog.component';

describe('CheckingIpsDialogComponent', () => {
  let component: CheckingIpsDialogComponent;
  let fixture: ComponentFixture<CheckingIpsDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckingIpsDialogComponent],
    });
    fixture = TestBed.createComponent(CheckingIpsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
