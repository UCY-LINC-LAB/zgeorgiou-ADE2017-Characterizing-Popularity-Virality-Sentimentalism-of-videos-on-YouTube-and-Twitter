import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import {GroupsResult} from '../models/groups-result';

@Injectable()
export class EndpointsServiceService {

  private HOST = "http://localhost:8000";

  constructor(private http: Http) { }


  /**
   * Load Groups endpoint
   */
  public loadGroups(category:number,lblWnd:number,percentage:number) : Observable<GroupsResult>{
    let url = `${this.HOST}/videos/groups?category=${category}&lbl_wnd=${lblWnd}&percentage=${percentage}`;
    return this.http.get(url)
        .map(this.extractGroupsResult)
        .catch(this.handleError);
  }

  private extractGroupsResult(res:Response) : GroupsResult{
    let body =  res.json();

    console.log(body.data)
    if(body.error.length!=0){
      throw new Error (body.error[0]);
    }
    return <GroupsResult>body.data;
  }

  private handleError(error: Response | any) {
    let errMsg: string;
    console.log(error)
    return Observable.throw("as");
  }
}
