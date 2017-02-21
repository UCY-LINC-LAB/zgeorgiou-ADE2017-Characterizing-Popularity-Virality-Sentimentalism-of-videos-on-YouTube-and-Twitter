import { TestBed, inject } from '@angular/core/testing';
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