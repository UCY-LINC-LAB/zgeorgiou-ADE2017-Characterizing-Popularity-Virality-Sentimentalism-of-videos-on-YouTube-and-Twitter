import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupsAnalysisComponent } from './groups-analysis.component';

describe('GroupsAnalysisComponent', () => {
  let component: GroupsAnalysisComponent;
  let fixture: ComponentFixture<GroupsAnalysisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GroupsAnalysisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupsAnalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
