import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-groups-analysis',
  templateUrl: './groups-analysis.component.html',
  styleUrls: ['./groups-analysis.component.css']
})
export class GroupsAnalysisComponent implements OnInit {

  private categories = [];
  private category = 0;

  constructor() { }

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

  }
}
