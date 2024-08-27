import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MobileScanIdComponent } from './mobile-scan-id.component';

describe('MobileScanIdComponent', () => {
  let component: MobileScanIdComponent;
  let fixture: ComponentFixture<MobileScanIdComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MobileScanIdComponent],
    });
    fixture = TestBed.createComponent(MobileScanIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
