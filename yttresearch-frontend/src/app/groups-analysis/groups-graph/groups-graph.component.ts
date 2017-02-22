import { Component, OnInit, Input,OnChanges} from '@angular/core';

@Component({
  selector: 'app-groups-graph',
  templateUrl: './groups-graph.component.html',
  styleUrls: ['./groups-graph.component.css']
})
export class GroupsGraphComponent implements OnInit,OnChanges {

  public ready;

  @Input()
  public loading : boolean;

  @Input()
  public error : boolean;

  @Input()
  public chart : boolean;

  constructor() { }

  ngOnInit() {
    this.ready = false;
  }
  ngOnChanges(){
    if(this.loading ==true){
      this.loading = false;
      }
  }
}
