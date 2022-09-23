import { TestBed } from '@angular/core/testing';

import { ResultFileUploadService } from './result-file-upload.service';

describe('ResultFileUploadService', () => {
  let service: ResultFileUploadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResultFileUploadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
