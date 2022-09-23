import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganisationRoleChooserComponent } from './organisation-role-chooser.component';

describe('OrganisationRoleChooserComponent', () => {
  let component: OrganisationRoleChooserComponent;
  let fixture: ComponentFixture<OrganisationRoleChooserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrganisationRoleChooserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrganisationRoleChooserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
