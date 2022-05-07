import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

const PROJ_API = 'http://localhost:8080/api/performance/';

@Injectable({
  providedIn: 'root'
})
export class PerformanceService {

  constructor(private http:HttpClient) { }

  getDataForTodayChart(orgId:any): Observable<any> {
    return this.http.get(PROJ_API + `task-today/${orgId}`);
  }

  getDataForSummaryChart(orgId:any): Observable<any> {
    return this.http.get(PROJ_API + `last-30-days/${orgId}`);
  }
}
