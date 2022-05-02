import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewBookmarkComponent } from './preview-bookmark.component';

describe('PreviewBookmarkComponent', () => {
  let component: PreviewBookmarkComponent;
  let fixture: ComponentFixture<PreviewBookmarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreviewBookmarkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreviewBookmarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
