import { Injectable } from '@angular/core';
import {HttpClient, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

const API = 'http://localhost:8080/api/teams/';

@Injectable({
  providedIn: 'root'
})
export class TeamsService {

  constructor(private http:HttpClient) { }

  getByOrganisationId(orgId:any,params:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}organisation/${orgId}`,{
      params
    });
  }

  getByOrganisationIdNoPage(orgId:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}organisation-nopage/${orgId}`);
  }

  deleteTeam(teamId:any): Observable<any> {
    return this.http.delete(API + `delete/${teamId}`);
  }

  save(formVal:any,teamMembers:any[]): Observable<any> {
    return this.http.post(`${API}save`,{
      team:formVal,
      teamMembers:teamMembers
    });
  }
}
