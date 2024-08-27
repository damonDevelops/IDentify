import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ExpandedDataModalComponent } from './expand-data-modal.component';

describe('ExpandedDataModalComponent', () => {
  let component: ExpandedDataModalComponent;
  let fixture: ComponentFixture<ExpandedDataModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExpandedDataModalComponent],
    });
    fixture = TestBed.createComponent(ExpandedDataModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
