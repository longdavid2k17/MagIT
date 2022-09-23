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

  getAllProjectsForOrg(orgId:any,search?:any): Observable<any> {
    let params = { id:orgId};
    if(search)
      { // @ts-ignore
        params = {id:orgId, search : search };
      }
    return this.http.get(PROJ_API + `get-all`,{
      params:params
    });
  }

  archiveProject(projId:any): Observable<any> {
    return this.http.get(PROJ_API + `archive/${projId}`);
  }

  deleteProject(projId:any): Observable<any> {
    return this.http.delete(PROJ_API + `delete/${projId}`);
  }

  save(formVal:any): Observable<any> {
    return this.http.post(PROJ_API + `save`,formVal);
  }
}
