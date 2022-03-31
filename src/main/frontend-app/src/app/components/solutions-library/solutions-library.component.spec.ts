import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolutionsLibraryComponent } from './solutions-library.component';

describe('SolutionsLibraryComponent', () => {
  let component: SolutionsLibraryComponent;
  let fixture: ComponentFixture<SolutionsLibraryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SolutionsLibraryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SolutionsLibraryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
