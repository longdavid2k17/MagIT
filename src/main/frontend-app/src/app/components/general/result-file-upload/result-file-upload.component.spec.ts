import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultFileUploadComponent } from './result-file-upload.component';

describe('ResultFileUploadComponent', () => {
  let component: ResultFileUploadComponent;
  let fixture: ComponentFixture<ResultFileUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResultFileUploadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResultFileUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
