import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppScanIdRedirectComponent } from './scan-id-redirect.component';

describe('ScanIdRedirectComponent', () => {
  let component: AppScanIdRedirectComponent;
  let fixture: ComponentFixture<AppScanIdRedirectComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppScanIdRedirectComponent],
    });
    fixture = TestBed.createComponent(AppScanIdRedirectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
