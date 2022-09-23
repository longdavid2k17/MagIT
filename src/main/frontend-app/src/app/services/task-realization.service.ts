import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpResponse} from "@angular/common/http";

const API = 'http://localhost:8080/api/realization/';

@Injectable({
  providedIn: 'root'
})
export class TaskRealizationService {

  constructor(private http:HttpClient) { }

  verifyRealizationPossibility(id:any):Observable<any>{
    return this.http.get<any>(`${API}verify/${id}`);
  }

  getRealizationStatus(id:any):Observable<any>{
    return this.http.get<any>(`${API}status/${id}`);
  }

  changeStatus(id:any):Observable<any>{
    return this.http.get<any>(`${API}change-status/${id}`);
  }
}
