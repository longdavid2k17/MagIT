import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttachmentFileUploadComponent } from './attachment-file-upload.component';

describe('AttachmentFileUploadComponent', () => {
  let component: AttachmentFileUploadComponent;
  let fixture: ComponentFixture<AttachmentFileUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AttachmentFileUploadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AttachmentFileUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
