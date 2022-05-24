import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

const API = 'http://localhost:8080/api/profiles/';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  getById(userId:any):Observable<any>{
    return this.http.get<any>(`${API}by-id/${userId}`);
  }

  getByOrganisationId(orgId:any,params?:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}by-organisation/${orgId}`,{
      params
    });
  }
  getByOrganisationIdNoPage(orgId:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}by-organisation-nopage/${orgId}`);
  }

  getByOrganisationIdAndRoleIdNoPage(orgId:any,roleId:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}by-organisation-and-role-nopage/${orgId}/${roleId}`);
  }

  save(profile: any) {
    return this.http.put<any>(`${API}save`,
      profile);
  }

  enableAccount(profile: any) {
    return this.http.get<any>(`${API}enable-account/`+profile.id);
  }

  disableAccount(profile: any) {
    return this.http.get<any>(`${API}disable-account/`+profile.id);
  }

  delete(profile: any) {
    return this.http.delete<any>(`${API}remove-account/`+profile.id);
  }
}
