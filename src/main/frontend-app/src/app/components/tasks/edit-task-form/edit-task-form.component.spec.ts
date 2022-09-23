import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTaskFormComponent } from './edit-task-form.component';

describe('EditTaskFormComponent', () => {
  let component: EditTaskFormComponent;
  let fixture: ComponentFixture<EditTaskFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditTaskFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditTaskFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
