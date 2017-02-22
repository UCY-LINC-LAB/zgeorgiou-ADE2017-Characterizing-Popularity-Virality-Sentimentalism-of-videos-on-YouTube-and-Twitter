import { TestBed, inject } from '@angular/core/testing';
import { GroupsAnalysisFormServiceService } from './groups-analysis-form-service.service';

describe('GroupsAnalysisFormServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GroupsAnalysisFormServiceService]
    });
  });

  it('should ...', inject([GroupsAnalysisFormServiceService], (service: GroupsAnalysisFormServiceService) => {
    expect(service).toBeTruthy();
  }));
});
