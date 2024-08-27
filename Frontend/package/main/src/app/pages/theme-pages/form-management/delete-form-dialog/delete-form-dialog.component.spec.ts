import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteFormDialogComponent } from './delete-form-dialog.component';

describe('DeleteFormDialogComponent', () => {
  let component: DeleteFormDialogComponent;
  let fixture: ComponentFixture<DeleteFormDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeleteFormDialogComponent],
    });
    fixture = TestBed.createComponent(DeleteFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
