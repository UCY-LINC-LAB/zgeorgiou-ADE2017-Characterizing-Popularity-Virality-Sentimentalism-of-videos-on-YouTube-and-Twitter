import { Component, OnInit ,Input,OnChanges } from '@angular/core';

import {GroupsResult} from '../../models/groups-result';
import {Group} from '../../models/group';
import {GroupDay} from '../../models/group-day';

@Component({
  selector: 'app-groups-stats',
  templateUrl: './groups-stats.component.html',
  styleUrls: ['./groups-stats.component.css']
})
export class GroupsStatsComponent implements OnInit,OnChanges{

@Input()
private data : GroupsResult;

@Input()
private groupsOption : string;

@Input()
private showErrorBars : boolean;

private likes_average ;

private groupsChart :Object;

private groupChart :Object;

private groups = [
  "Popular",
  "Viral",
  "Recent",
  "Random",
  "Popular and Viral",
  "Popular but not Viral",
  "Viral but not Popular",
]
private group = "Popular";

private options = [
  "Average",
  "Median",
  "Error Bars",
]
private option = "Average";


  constructor() { }

  ngOnInit() {
  }

  ngOnChanges() {
    if (!this.data)
      return;

    if (this.groupsOption == "Views") {

      if (this.showErrorBars)
        this.groupsChart = this.prepareGraph("Views", this.getGroupsError("views_added"));
      else
        this.groupsChart = this.prepareGraph("Views", this.getGroupsAverage("views_added"));
      
      if (this.option == "Average"){
        //this.groupChart = this.prepareGroupChart("Views", "views_added");
      }

    }


    if (this.groupsOption == "Tweets") {
      if (this.showErrorBars)
        this.groupsChart = this.prepareGraph("Tweets", this.getGroupsError("tweets_added"));
      else
        this.groupsChart = this.prepareGraph("Tweets", this.getGroupsAverage("tweets_added"));
    }

    if (this.groupsOption == "Likes") {
      if (this.showErrorBars)
        this.groupsChart = this.prepareGraph("Likes", this.getGroupsError("likes_added"));
      else
        this.groupsChart = this.prepareGraph("Likes", this.getGroupsAverage("likes_added"));
    }

    if (this.groupsOption == "Dislikes"){
      if (this.showErrorBars)
        this.groupsChart = this.prepareGraph("Dislikes", this.getGroupsError("dislikes_added"));
      else
        this.groupsChart = this.prepareGraph("Dislikes", this.getGroupsAverage("dislikes_added"));
    }
    if (this.groupsOption == "Likes Ratio"){
      if (this.showErrorBars)
        this.groupsChart = this.prepareGraph("Likes Ratio", this.getGroupsError("ratio_likes"));
      else
        this.groupsChart = this.prepareGraph("Likes Ratio", this.getGroupsAverage("ratio_likes"));
    }

    if (this.groupsOption == "Tweets Sentiment-Neutral"){
      if (this.showErrorBars)
        this.groupsChart = this.prepareGraph("Tweets Sentiment-Neutral", this.getGroupsError("tweets_sentiment_neu"));
      else
        this.groupsChart = this.prepareGraph("Tweets Sentiment-Neutral", this.getGroupsAverage("tweets_sentiment_neu"));
    }
}


  getGroupsAverage(key:string){
    return [
        this.getGroupAverage("Popular",key,this.data.groups.popular),
        this.getGroupAverage("Viral",key,this.data.groups.viral),
        this.getGroupAverage("Recent",key,this.data.groups.recent),
        this.getGroupAverage("Random",key,this.data.groups.random),
        this.getGroupAverage("Popular and Viral",key,this.data.groups.popular_viral),
        this.getGroupAverage("Popular but not Viral",key,this.data.groups.popular_not_viral),
        this.getGroupAverage("Viral but not Popular",key,this.data.groups.viral_not_popular),
    ]

  }
  getGroupsError(key:string){
    return [
        this.getGroupAverage("Popular",key,this.data.groups.popular,true),
        this.getGroupErrorBar("Popular Error",key,this.data.groups.popular),

        this.getGroupAverage("Viral",key,this.data.groups.viral,false),
        this.getGroupErrorBar("Viral Error",key,this.data.groups.viral),

        this.getGroupAverage("Recent",key,this.data.groups.recent,false),
        this.getGroupErrorBar("Recent Error",key,this.data.groups.recent),

        this.getGroupAverage("Random",key,this.data.groups.random,false),
        this.getGroupErrorBar("Random Error",key,this.data.groups.random),

        this.getGroupAverage("Popular and Viral",key,this.data.groups.popular_viral,false),
        this.getGroupErrorBar("Popular and Viral Error",key,this.data.groups.popular_viral),

        this.getGroupAverage("Popular but not Viral",key,this.data.groups.popular_not_viral,false),
        this.getGroupErrorBar("Popular but not Viral Error",key,this.data.groups.popular_not_viral),

        this.getGroupAverage("Viral but not Popular",key,this.data.groups.viral_not_popular,false),
        this.getGroupErrorBar("Viral but not Popular Error",key,this.data.groups.viral_not_popular),
    ]
  }






  /** HELPERS */
  getGroupAverage(name:string,key:string,group : Group, visible?:boolean){
    let v;
    if(typeof visible == undefined) v = true; else v = visible;

    return {
      name: name,
      type: 'spline',
      data : group.days.map(day => [day.day, day[key].average]),
      visible : v,
      tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name} </span>: <b>{point.y:.1f}</b> ' }

    };
  }
  getGroupErrorBar(name:string,key:string,group : Group){
    let data = []
    for (let i = 0; i <group.days[0].day; i++) {
      data.push([null,null])
    }

    let p = (group.days.map(day => {
      let half = day[key].std / 2;
      let avg = day[key].average;
      return [avg - half, avg + half];
    }));
    data = data.concat(p)
    return {
      name: name,
      type: 'errorbar',
      data : data,
      tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name} range</span>: <b>{point.low:.2f}-{point.high:.2f}</b> ' }

    };
  }

  prepareGraph(title:String, data :any){
    return {
      title : { text : title},
      series: data,
      exporting: { enabled: true }
    };
  }

  prepareGroupChart(title:String, key:any){
    /**
    if(this.option == "Average"){
      if(this.group =="Popular")
      */
  }
}
