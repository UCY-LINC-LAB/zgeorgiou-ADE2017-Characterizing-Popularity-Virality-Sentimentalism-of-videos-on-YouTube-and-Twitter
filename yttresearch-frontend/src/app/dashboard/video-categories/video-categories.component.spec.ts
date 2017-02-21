import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VideoCategoriesComponent } from './video-categories.component';

describe('VideoCategoriesComponent', () => {
  let component: VideoCategoriesComponent;
  let fixture: ComponentFixture<VideoCategoriesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VideoCategoriesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VideoCategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
