import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageTeamMembershipComponent } from './manage-team-membership.component';

describe('ManageTeamMembershipComponent', () => {
  let component: ManageTeamMembershipComponent;
  let fixture: ComponentFixture<ManageTeamMembershipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageTeamMembershipComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageTeamMembershipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
