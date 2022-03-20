import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

const API = 'http://localhost:8080/api/messenger/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class MessengerService {

  constructor(private http: HttpClient) { }

  getAll(id:any):Observable<any>{
    return this.http.get<any>(`${API}contacts/${id}`);
  }
}
