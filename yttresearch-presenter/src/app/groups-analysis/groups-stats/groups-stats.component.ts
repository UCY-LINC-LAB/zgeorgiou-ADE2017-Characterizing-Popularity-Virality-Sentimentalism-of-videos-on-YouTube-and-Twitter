import { Component, OnInit ,Input,OnChanges } from '@angular/core';

import {GroupsResult} from '../../models/groups-result';
import {Group} from '../../models/group';

@Component({
  selector: 'app-groups-stats',
  templateUrl: './groups-stats.component.html',
  styleUrls: ['./groups-stats.component.css']
})
export class GroupsStatsComponent implements OnInit,OnChanges{

@Input()
private data : GroupsResult;

@Input()
private option : string;

private likes_average ;

private options :Object;

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(){
    if(this.data){
      if (this.option == "Views")
        this.options = this.prepareGraph("Views", this.getGroupViewsAverage());
      if (this.option == "Tweets")
        this.options = this.prepareGraph("Tweets", this.getGroupTweetsAverage());
      if (this.option == "Likes")
        this.options = this.prepareGraph("Likes", this.getLikesAverage());
      if (this.option == "Dislikes")
        this.options = this.prepareGraph("Dislikes", this.getGroupDislakesAverage());
    }
  }

  getGroupViewsAverage(){
    return [
        this.getGroupsAverage("Popular","views_added",this.data.groups.popular),
        this.getGroupsAverage("Viral","views_added",this.data.groups.viral),
        this.getGroupsAverage("Recent","views_added",this.data.groups.recent),
        this.getGroupsAverage("Random","views_added",this.data.groups.random),
        this.getGroupsAverage("Popular and Viral","views_added",this.data.groups.popular_viral),
        this.getGroupsAverage("Popular but not Viral","views_added",this.data.groups.popular_not_viral),
        this.getGroupsAverage("Viral but not Popular","views_added",this.data.groups.viral_not_popular),
    ]
  }
  getGroupTweetsAverage(){
    return [
        this.getGroupsAverage("Popular","tweets_added",this.data.groups.popular),
        this.getGroupsAverage("Viral","tweets_added",this.data.groups.viral),
        this.getGroupsAverage("Recent","tweets_added",this.data.groups.recent),
        this.getGroupsAverage("Random","tweets_added",this.data.groups.random),
        this.getGroupsAverage("Popular and Viral","tweets_added",this.data.groups.popular_viral),
        this.getGroupsAverage("Popular but not Viral","tweets_added",this.data.groups.popular_not_viral),
        this.getGroupsAverage("Viral but not Popular","tweets_added",this.data.groups.viral_not_popular),
    ]
  }
  getLikesAverage(){
    return [
        this.getGroupLikesAverage("Popular",this.data.groups.popular),
        this.getGroupLikesAverage("Viral",this.data.groups.viral),
        this.getGroupLikesAverage("Recent",this.data.groups.recent),
        this.getGroupLikesAverage("Random",this.data.groups.random),
        this.getGroupLikesAverage("Popular and Viral",this.data.groups.popular_viral),
        this.getGroupLikesAverage("Popular but not Viral",this.data.groups.popular_not_viral),
        this.getGroupLikesAverage("Viral but not Popular",this.data.groups.viral_not_popular),
    ]
  }
  getGroupDislakesAverage(){
    return [
        this.getGroupsAverage("Popular","dislikes_added",this.data.groups.popular),
        this.getGroupsAverage("Viral","dislikes_added",this.data.groups.viral),
        this.getGroupsAverage("Recent","dislikes_added",this.data.groups.recent),
        this.getGroupsAverage("Random","dislikes_added",this.data.groups.random),
        this.getGroupsAverage("Popular and Viral","dislikes_added",this.data.groups.popular_viral),
        this.getGroupsAverage("Popular but not Viral","dislikes_added",this.data.groups.popular_not_viral),
        this.getGroupsAverage("Viral but not Popular","dislikes_added",this.data.groups.viral_not_popular),
    ]
  }

  getGroupsAverage(name:string,key:string,group : Group){
    return {
      name: name,
      type: 'spline',
      data : group.days.map(day => [day.day, day[key].average]),
      tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name} </span>: <b>{point.y:.1f}</b> ' }

    };
  }

  getGroupLikesAverage(name:string,group : Group){
    return {
      name: name,
      type: 'spline',
      data : group.days.map(day => [day.day, day.likes_added.average]),
      tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name} </span>: <b>{point.y:.1f}</b> ' }

    };
  }

  prepareGraph(title:String, data :any){
    return {
      title : { text : title},
      series: data,
      exporting: { enabled: true }
    };
  }
}
