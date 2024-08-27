import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HowDoesThisWorkDialogComponent } from './how-does-this-work-dialog.component';

describe('HowDoesThisWorkDialogComponent', () => {
  let component: HowDoesThisWorkDialogComponent;
  let fixture: ComponentFixture<HowDoesThisWorkDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HowDoesThisWorkDialogComponent],
    });
    fixture = TestBed.createComponent(HowDoesThisWorkDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
