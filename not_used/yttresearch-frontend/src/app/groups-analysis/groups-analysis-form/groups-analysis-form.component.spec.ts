import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupsAnalysisFormComponent } from './groups-analysis-form.component';

describe('GroupsAnalysisFormComponent', () => {
  let component: GroupsAnalysisFormComponent;
  let fixture: ComponentFixture<GroupsAnalysisFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GroupsAnalysisFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupsAnalysisFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
