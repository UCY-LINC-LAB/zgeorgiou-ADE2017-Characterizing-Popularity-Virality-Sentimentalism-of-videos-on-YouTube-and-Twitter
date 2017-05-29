import { Injectable } from '@angular/core';

import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';


@Injectable()
export class EndpointsService {

  private HOST = "http://localhost:8000";


  constructor(private http:Http) { }


  /**
   * Load Videos endpoint
   */
  public loadCategories() : Observable<any>{
    let url = `${this.HOST}/videos`;
    return this.http.get(url)
        .map(this.extractData)
        .catch(this.handleError);
  }

  /**
   * Load Videos endpoint
   */
  public loadGroup(group:string,category:number,lblWnd:number,percentage:number) : Observable<any>{
    let url = `${this.HOST}/videos/${group}?category=${category}&lbl_wnd=${lblWnd}&percentage=${percentage}`;
    return this.http.get(url)
        .map(this.extractData)
        .catch(this.handleError);
  }

  private extractData(res:Response){
    let body = res.json();
    if(body.errors.length!=0){
      throw new Error (body.errors[0]);
    }
    return body.data || {};
  }
  private handleError(error: Response | any) {
    let errMsg: string;
    console.log(error)
    return Observable.throw(error);
    /*
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.errors[0] || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    return Observable.throw(errMsg);*/
  }
}
