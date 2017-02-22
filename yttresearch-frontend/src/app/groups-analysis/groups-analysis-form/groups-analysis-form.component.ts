import { Component, OnInit, Output, EventEmitter } from '@angular/core';


import {EndpointsService} from '../../utils/endpoints.service';

@Component({
  selector: 'app-groups-analysis-form',
  templateUrl: './groups-analysis-form.component.html',
  styleUrls: ['./groups-analysis-form.component.css'],
})
export class GroupsAnalysisFormComponent implements OnInit {

  private error = false;

  private categories = [];
  private category : number;
  private lblWnd : number;
  private percentage : number;

  constructor(private endpointsSerice:EndpointsService) { }

  ngOnInit() {
    this.lblWnd = 3;
    this.percentage = 2.5;
    this.category = 0;

    this.loadCategories();
  }

  private submit(){
    this.endpointsSerice.loadPopularGroup()
      .subscribe(data=>{
        this.data.emit(data);
      },error => this.error=true);

  }
  @Output() data : EventEmitter<any> = new EventEmitter(); 

  
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
              console.log(data);


            },
            err => { this.error=true; }
    );
  }
}
