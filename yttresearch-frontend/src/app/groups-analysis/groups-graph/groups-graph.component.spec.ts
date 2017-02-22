import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupsGraphComponent } from './groups-graph.component';

describe('GroupsGraphComponent', () => {
  let component: GroupsGraphComponent;
  let fixture: ComponentFixture<GroupsGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GroupsGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupsGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
