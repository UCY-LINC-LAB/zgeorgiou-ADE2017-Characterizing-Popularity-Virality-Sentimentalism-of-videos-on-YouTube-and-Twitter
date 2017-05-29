import { Component, OnInit } from '@angular/core';

import { EndpointsService } from '../../utils/endpoints.service';
import {VideoCategories} from './video-categories';

@Component({
  selector: 'app-video-categories',
  templateUrl: './video-categories.component.html',
  styleUrls: ['./video-categories.component.css'],
  providers: [EndpointsService,]
})
export class VideoCategoriesComponent implements OnInit {

private loading : boolean;
private error : boolean;

  chartOptions = {}

  public constructor(private EndpointsService : EndpointsService) {
   }

  ngOnInit() {

    this.loadCategories();
  }

  private loadCategories(){
    this.loading = true;
    this.error = false;

    this.EndpointsService.loadCategories()
        .subscribe( data => this.dataFetched(data), error => this.errorFetched(error));
  }

  private dataFetched(data : any){
    this.chartOptions =  new VideoCategories(data).getGraph();

    this.loading = false; //Data is loaded
  }
  private errorFetched(error : any){
    this.error = true;
  }
}
