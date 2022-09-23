import { TestBed } from '@angular/core/testing';

import { OrganisationRolesService } from './organisation-roles.service';

describe('OrganisationRolesService', () => {
  let service: OrganisationRolesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganisationRolesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
