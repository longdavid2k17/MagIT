import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";


const PROJ_API = 'http://localhost:8080/api/projects/';

@Injectable({
  providedIn: 'root'
})
export class ProjectsService {

  constructor(private http: HttpClient) { }

  getAllPMsFromOrg(orgId:any): Observable<any> {
    return this.http.get(PROJ_API + `get-all-pms/${orgId}`);
  }

  getAllProjectsForOrg(orgId:any): Observable<any> {
    return this.http.get(PROJ_API + `get-all/${orgId}`);
  }

  save(formVal:any): Observable<any> {
    return this.http.post(PROJ_API + `save`,formVal);
  }
}
