import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectTasksRegistryComponent } from './project-tasks-registry.component';

describe('ProjectTasksRegistryComponent', () => {
  let component: ProjectTasksRegistryComponent;
  let fixture: ComponentFixture<ProjectTasksRegistryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjectTasksRegistryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectTasksRegistryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
