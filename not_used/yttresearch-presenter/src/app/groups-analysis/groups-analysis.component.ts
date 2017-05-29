import { Component, OnInit } from '@angular/core';

import {EndpointsServiceService} from '../utils/endpoints-service.service';
import {GroupsResult} from '../models/groups-result';

@Component({
  selector: 'app-groups-analysis',
  templateUrl: './groups-analysis.component.html',
  styleUrls: ['./groups-analysis.component.css']
})
export class GroupsAnalysisComponent implements OnInit {

  private error : string;
  private categories = [];
  private category = 0;
  private lbl_wnd = 1;
  private percentage = 2.5;

  private  groupsResult: GroupsResult;

  private groupsOptions = [
    "Views",
    "Tweets",
    "Likes",
    "Dislikes",
    "Likes Ratio",
    "Tweets Sentiment-Neutral",

  ]

  groupsOption =  'Views';

  errorBars = false;

  constructor(private service : EndpointsServiceService) { }

  ngOnInit() {

    this.categories = [
      {id:0, name: "All"},
      {id:1, name: "Music"},
      {id:2, name: "Games"},
      {id:3, name: "People & Blogs"},
      {id:4, name: "Entertainment"},
      {id:5, name: "News & Politics"},
      {id:6, name: "Others"},
    ];

  }

  private submit(){
    let lblWnd = Number(this.lbl_wnd)
    if (lblWnd<0 || lblWnd>14) {
      this.error = "Label window must be between 0 and 14";
      this.lbl_wnd=1;
      return ;
    }
    let perc = Number(this.percentage) / 100;
    if (!perc || perc >=1 || perc <=0) {
      this.error = "Percentage must be a float between 0 and 100";
      this.percentage=2.5;
      return ;
    }
    this.service.loadGroups(this.category,lblWnd,perc)
      .subscribe(data => {
        this.groupsResult = data;
      },error => this.error = "Error with request");
  }
}
