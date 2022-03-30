import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const API = 'http://localhost:8080/api/organisation-roles/';

@Injectable({
  providedIn: 'root'
})
export class OrganisationRolesService {

  constructor(private http:HttpClient) { }

  getByOrganisationId(orgId:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}organisation/${orgId}`);
  }

  save(role:any):Observable<any>
  {
    return this.http.post<any>(`${API}save`,
      role);
  }
}