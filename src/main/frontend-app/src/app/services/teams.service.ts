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

  getAllUserTeams(userId:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}all-user-teams-nopage/${userId}`)
  }

  getTeamMembersByTeamId(teamId:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}teammembers-nopage/${teamId}`);
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

  edit(form: any, teamMembers: any[]): Observable<any> {
    return this.http.patch(`${API}edit`,{
      team:form,
      teamMembers:teamMembers
    });
  }
}
