import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-groups-analysis',
  templateUrl: './groups-analysis.component.html',
  styleUrls: ['./groups-analysis.component.css'],
})
export class GroupsAnalysisComponent implements OnInit {

  public graphLoading;
  public error;

  constructor() { }

  ngOnInit() {
    this.error = false;
  }

  getData(data :any){
    console.log('From Parent:',data);
    this.graphLoading = true;
    this.error = false;
    let total_videos = data.total_videos;
    let stats = data.stats;
  }
}
