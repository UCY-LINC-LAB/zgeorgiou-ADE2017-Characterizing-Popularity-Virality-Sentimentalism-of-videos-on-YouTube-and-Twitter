import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupsStatsComponent } from './groups-stats.component';

describe('GroupsStatsComponent', () => {
  let component: GroupsStatsComponent;
  let fixture: ComponentFixture<GroupsStatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GroupsStatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupsStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
