import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddExampleBookmarkComponent } from './add-example-bookmark.component';

describe('AddExampleBookmarkComponent', () => {
  let component: AddExampleBookmarkComponent;
  let fixture: ComponentFixture<AddExampleBookmarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddExampleBookmarkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddExampleBookmarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
