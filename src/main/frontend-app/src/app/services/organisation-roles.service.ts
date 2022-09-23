import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

const API = 'http://localhost:8080/api/organisation-roles/';

@Injectable({
  providedIn: 'root'
})
export class OrganisationRolesService {

  constructor(private http:HttpClient) { }

  getByOrganisationId(orgId:any,params?:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}organisation/${orgId}`,{params});
  }

  checkRoleDeletion(roleId:any):Observable<any>{
    return this.http.get<any>(`${API}check-for-deletion/${roleId}`);
  }

  deleteRole(roleId:any):Observable<any>{
    return this.http.delete<any>(`${API}delete/${roleId}`);
  }

  getByOrganisationIdToList(orgId:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}organisation-list/${orgId}`);
  }

  save(role:any):Observable<any>
  {
    return this.http.post<any>(`${API}save`,
      role);
  }

  saveToUser(roles:any,id:any):Observable<any> {
    return this.http.post<any>(`${API}save-for-user/${id}`,
      roles);
  }
}
