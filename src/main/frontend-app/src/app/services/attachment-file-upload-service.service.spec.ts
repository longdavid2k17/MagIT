import { TestBed } from '@angular/core/testing';

import { AttachmentFileUploadService } from './attachment-file-upload.service';

describe('AttachmentFileUploadServiceService', () => {
  let service: AttachmentFileUploadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttachmentFileUploadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
