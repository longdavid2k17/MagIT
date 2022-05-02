import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditExampleBookmarkComponent } from './edit-example-bookmark.component';

describe('EditExampleBookmarkComponent', () => {
  let component: EditExampleBookmarkComponent;
  let fixture: ComponentFixture<EditExampleBookmarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditExampleBookmarkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditExampleBookmarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
