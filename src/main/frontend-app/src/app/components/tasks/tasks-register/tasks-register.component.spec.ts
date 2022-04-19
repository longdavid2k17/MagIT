import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TasksRegisterComponent } from './tasks-register.component';

describe('TasksRegisterComponent', () => {
  let component: TasksRegisterComponent;
  let fixture: ComponentFixture<TasksRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TasksRegisterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TasksRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
