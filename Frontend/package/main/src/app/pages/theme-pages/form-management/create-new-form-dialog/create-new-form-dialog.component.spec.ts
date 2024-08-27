import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateNewFormDialogComponent } from './create-new-form-dialog.component';

describe('CreateNewFormDialogComponent', () => {
  let component: CreateNewFormDialogComponent;
  let fixture: ComponentFixture<CreateNewFormDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateNewFormDialogComponent],
    });
    fixture = TestBed.createComponent(CreateNewFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
