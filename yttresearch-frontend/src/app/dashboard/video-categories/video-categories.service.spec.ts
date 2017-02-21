/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { VideoCategoriesService } from './video-categories.service';

describe('VideoCategoriesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [VideoCategoriesService]
    });
  });

  it('should ...', inject([VideoCategoriesService], (service: VideoCategoriesService) => {
    expect(service).toBeTruthy();
  }));
});
