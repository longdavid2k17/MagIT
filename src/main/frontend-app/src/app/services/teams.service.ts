import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const API = 'http://localhost:8080/api/teams/';

@Injectable({
  providedIn: 'root'
})
export class TeamsService {

  constructor(private http:HttpClient) { }

  getByOrganisationId(orgId:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}organisation/${orgId}`);
  }
}
