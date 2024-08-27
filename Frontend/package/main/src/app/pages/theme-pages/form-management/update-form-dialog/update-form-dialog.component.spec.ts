import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateFormDialogComponent } from './update-form-dialog.component';

describe('UpdateFormDialogComponent', () => {
  let component: UpdateFormDialogComponent;
  let fixture: ComponentFixture<UpdateFormDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateFormDialogComponent],
    });
    fixture = TestBed.createComponent(UpdateFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
