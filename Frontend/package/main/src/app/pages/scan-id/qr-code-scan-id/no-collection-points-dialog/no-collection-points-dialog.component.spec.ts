import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoCollectionPointsDialogComponent } from './no-collection-points-dialog.component';

describe('NoCollectionPointsDialogComponent', () => {
  let component: NoCollectionPointsDialogComponent;
  let fixture: ComponentFixture<NoCollectionPointsDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NoCollectionPointsDialogComponent],
    });
    fixture = TestBed.createComponent(NoCollectionPointsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
