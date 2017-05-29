import { Component, OnInit } from '@angular/core';
import {DailyStats} from './groups-graph/daily-stats';
@Component({
  selector: 'app-groups-analysis',
  templateUrl: './groups-analysis.component.html',
  styleUrls: ['./groups-analysis.component.css'],
})
export class GroupsAnalysisComponent implements OnInit {

  private graphLoading;
  private error;
  private errorMsg;
  private chart;


  constructor() { }

  ngOnInit() {
    this.error = false;
  }

  getData(data :any){
    this.graphLoading = true;
    this.error = false;
    let total_videos = data.total_videos;
    let daily = data.stats.daily;
    let dailyStatsObj  = new DailyStats(total_videos,daily);

    this.chart = dailyStatsObj.getGraph();


  }

  getError(error: any){
    this.error = true;
    this.errorMsg = error;

  }
}
