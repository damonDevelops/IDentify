import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpandDataModalMobileComponent } from './expand-data-modal-mobile.component';

describe('ExpandDataModalMobileComponent', () => {
  let component: ExpandDataModalMobileComponent;
  let fixture: ComponentFixture<ExpandDataModalMobileComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExpandDataModalMobileComponent],
    });
    fixture = TestBed.createComponent(ExpandDataModalMobileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
