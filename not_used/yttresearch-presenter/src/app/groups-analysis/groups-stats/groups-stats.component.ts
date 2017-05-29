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



  constructor() { }

  ngOnInit() {
  }

  updateGroup(){
    if (!this.data)
      return;

    if (this.groupsOption == "Views")
      this.groupChart = this.prepareGroupChart("Views", "views_added");
    else if (this.groupsOption == "Tweets")
      this.groupChart = this.prepareGroupChart("Tweets", "tweets_added");
    else if (this.groupsOption == "Likes")
      this.groupChart = this.prepareGroupChart("Likes", "likes_added");
    else if (this.groupsOption == "Dislikes")
      this.groupChart = this.prepareGroupChart("Dislikes", "dislikes_added");
    else if (this.groupsOption == "Likes Ratio")
      this.groupChart = this.prepareGroupChart("Likes Ratio", "likes_ratio");
    else if (this.groupsOption == "Tweets Sentiment-Neutral")
      this.groupChart = this.prepareGroupChart("Tweets Sentiment-Neutral", "tweets_sentiment_neu");

  }
  ngOnChanges() {
    if (!this.data)
      return;

    if (this.groupsOption == "Views") 
        this.groupsChart = this.prepareGraph("Views", this.getGroupsAverage("views_added"));

    if (this.groupsOption == "Tweets") {
      this.groupsChart = this.prepareGraph("Tweets", this.getGroupsAverage("tweets_added"));
    }

    if (this.groupsOption == "Likes") {
      this.groupsChart = this.prepareGraph("Likes", this.getGroupsAverage("likes_added"));
    }

    if (this.groupsOption == "Dislikes"){
      this.groupsChart = this.prepareGraph("Dislikes", this.getGroupsAverage("dislikes_added"));
    }
    if (this.groupsOption == "Likes Ratio"){
      this.groupsChart = this.prepareGraph("Likes Ratio", this.getGroupsAverage("ratio_likes"));
    }

    if (this.groupsOption == "Tweets Sentiment-Neutral"){
      this.groupsChart = this.prepareGraph("Tweets Sentiment-Neutral", this.getGroupsAverage("tweets_sentiment_neu"));
    }

    this.updateGroup();
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
    if(typeof visible === 'undefined') v = true; else v = visible;

    return {
      name: name,
      type: 'spline',
      data : group.days.map(day => [day.day, day[key].average]),
      visible : v,
      tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name} </span>: <b>{point.y:.1f}</b> ' }

    };
  }
  /** HELPERS */
  getGroupMedian(name:string,key:string,group : Group, visible?:boolean){
    let v;
    if(typeof visible == 'undefined') v = true; else v = visible;

    return {
      name: name,
      type: 'spline',
      data : group.days.map(day => [day.day, day[key].median]),
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

  getGroupStats(title:string, key :any,group:Group){
    return {
      title : { text : title},
      series: [
        this.getGroupAverage("Average",key,group),
        this.getGroupErrorBar("Average",key,group),
        this.getGroupMedian("Median",key,group),

      ],
      exporting: { enabled: true }
    };
  }

  prepareGroupChart(title:string, key:any){
    let data = this.getGroupAverage(title, key, this.data.groups.popular)
    if (this.group == "Popular") 
        return this.getGroupStats(title,key,this.data.groups.popular)

    if (this.group == "Viral") 
        return this.getGroupStats(title,key,this.data.groups.viral)

    if (this.group == "Recent") 
        return this.getGroupStats(title,key,this.data.groups.recent)

    if (this.group == "Random") 
        return this.getGroupStats(title,key,this.data.groups.random)

    if (this.group == "Popular and Viral") 
        return this.getGroupStats(title,key,this.data.groups.popular_viral)

    if (this.group == "Popular but not Viral") 
        return this.getGroupStats(title,key,this.data.groups.popular_not_viral)

    if (this.group == "Viral but not Popular") 
        return this.getGroupStats(title,key,this.data.groups.viral_not_popular)

  }
}
