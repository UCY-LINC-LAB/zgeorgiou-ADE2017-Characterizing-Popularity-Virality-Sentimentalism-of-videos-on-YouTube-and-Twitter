import { TestBed, inject } from '@angular/core/testing';

import { EndpointsServiceService } from './endpoints-service.service';

describe('EndpointsServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EndpointsServiceService]
    });
  });

  it('should ...', inject([EndpointsServiceService], (service: EndpointsServiceService) => {
    expect(service).toBeTruthy();
  }));
});
