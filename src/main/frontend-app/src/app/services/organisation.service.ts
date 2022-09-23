import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const API = 'http://localhost:8080/api/organisations/';

@Injectable({
  providedIn: 'root'
})
export class OrganisationService {

  constructor(private http:HttpClient) { }

  getByOwnerId(id:any):Observable<any>{
    return this.http.get<any>(`${API}owner-id/${id}`);
  }

  save(organisation:any):Observable<any>
  {
    return this.http.post<any>(`${API}save`,
      organisation);
  }
}
