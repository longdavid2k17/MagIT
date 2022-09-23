import { TestBed } from '@angular/core/testing';

import { UserEmulationInterceptor } from './user-emulation.interceptor';

describe('UserEmulationInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      UserEmulationInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: UserEmulationInterceptor = TestBed.inject(UserEmulationInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
