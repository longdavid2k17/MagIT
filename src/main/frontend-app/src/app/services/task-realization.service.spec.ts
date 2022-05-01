import { TestBed } from '@angular/core/testing';

import { TaskRealizationService } from './task-realization.service';

describe('TaskRealizationService', () => {
  let service: TaskRealizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TaskRealizationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
