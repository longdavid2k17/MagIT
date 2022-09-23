import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamEditFormComponent } from './team-edit-form.component';

describe('TeamEditFormComponent', () => {
  let component: TeamEditFormComponent;
  let fixture: ComponentFixture<TeamEditFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TeamEditFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamEditFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
