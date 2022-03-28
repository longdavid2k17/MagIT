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

  save(profile: any) {
    return this.http.put<any>(`${API}save`,
      profile);
  }
}
