import { Component, OnInit, Output, EventEmitter } from '@angular/core';


import {EndpointsService} from '../../utils/endpoints.service';

@Component({
  selector: 'app-groups-analysis-form',
  templateUrl: './groups-analysis-form.component.html',
  styleUrls: ['./groups-analysis-form.component.css'],
})
export class GroupsAnalysisFormComponent implements OnInit {

  private categories = [];
  private category : number;
  private lblWnd : number;
  private percentage : number;

  private groups = [
    {name:"Popular Videos", value:"popular"},
    {name:"Viral Videos", value:"viral"},
    {name:"Recent Videos", value:"recent"},
    {name:"Random Videos", value:"random"},
  ];
  private group : string;
  constructor(private endpointsSerice:EndpointsService) { }

  ngOnInit() {
    this.lblWnd = 3;
    this.percentage = 2.5;
    this.category = 0;
    this.group = "popular";

    this.loadCategories();
  }

  private submit(){
    let lblWnd = Number(this.lblWnd)
    if (lblWnd<=0 || lblWnd>14) {
      this.error.emit("Label window must be between 1 and 14");
      return ;
    }
    
    let perc = Number(this.percentage) / 100;
    console.log(perc)
    if (!perc || perc>=1 || perc<=0) {
      this.error.emit("Percentage must be a float number between 0 and 1");
      return ;
    }
    this.endpointsSerice.loadGroup(this.group,this.category,this.lblWnd,perc)
      .subscribe(data=>{
        this.data.emit(data);
      },error => this.error.emit(error.message));

  }
  @Output() data : EventEmitter<any> = new EventEmitter(); 
  @Output() error : EventEmitter<string> = new EventEmitter(); 

  
  private loadCategories(){
    this.endpointsSerice.loadCategories()
        .subscribe(
            data => { 
              this.categories = [];
              let categories = data.categories;
              let i=0;
              this.categories.push({name:'All',id:i});
              for(let category of categories){
                i++;
                this.categories.push({name:category['name'],id:i});
              }


            },
            err => { this.error.emit(err.message)}
    );
  }
}
