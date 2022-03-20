import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganisationManagementComponent } from './organisation-management.component';

describe('OrganisationManagementComponent', () => {
  let component: OrganisationManagementComponent;
  let fixture: ComponentFixture<OrganisationManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrganisationManagementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrganisationManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
