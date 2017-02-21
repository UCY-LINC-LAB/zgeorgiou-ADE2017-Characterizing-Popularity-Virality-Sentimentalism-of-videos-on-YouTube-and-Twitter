import { Injectable } from '@angular/core';

import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import {Endpoints} from '../../utils/endpoints';

@Injectable()
export class VideoCategoriesService {

  constructor(private http:Http) { }


  public loadCategories() : Observable<any>{
    let url = `${Endpoints.HOST}/videos`;

    return this.http.get(url)
        .map(this.extractData)
        .catch(this.handleError);
  }

  private extractData(res:Response){
    let body = res.json();
    return body.data || {};
  }
  private handleError(error: Response | any) {
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.errors[0] || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    return Observable.throw(errMsg);
  }
}
