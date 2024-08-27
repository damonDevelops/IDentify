import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeactivatedCollectionPointDialogComponent } from './deactivated-collection-point-dialog.component';

describe('DeactivatedCollectionPointDialogComponent', () => {
  let component: DeactivatedCollectionPointDialogComponent;
  let fixture: ComponentFixture<DeactivatedCollectionPointDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeactivatedCollectionPointDialogComponent],
    });
    fixture = TestBed.createComponent(
      DeactivatedCollectionPointDialogComponent,
    );
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
