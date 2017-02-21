import { Component, OnInit } from '@angular/core';

import { VideoCategoriesService } from './video-categories.service';
import {VideoCategories} from './video-categories';

@Component({
  selector: 'app-video-categories',
  templateUrl: './video-categories.component.html',
  styleUrls: ['./video-categories.component.css'],
  providers: [VideoCategoriesService,]
})
export class VideoCategoriesComponent implements OnInit {

private loading : boolean;
private error : boolean;

  chartOptions = {}

  public constructor(private videoCategoriesService : VideoCategoriesService) {
   }

  ngOnInit() {

    this.loadCategories();
  }

  private loadCategories(){
    this.loading = true;
    this.error = false;

    this.videoCategoriesService.loadCategories()
        .subscribe( data => this.dataFetched(data), error => this.errorFetched(error));
  }

  private dataFetched(data : any){
    console.log(data);

    this.chartOptions =  new VideoCategories(data).getGraph();
    console.log(this.chartOptions);

    this.loading = false; //Data is loaded
  }
  private errorFetched(error : any){
    this.error = true;
  }
}
