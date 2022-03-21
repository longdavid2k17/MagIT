import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
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

  setAsRead(userId:any,interlocutorId:any):Observable<any>{
    return this.http.get<any>(`${API}set-as-read/${userId}/${interlocutorId}`);
  }

  send(userId:any,interlocutorId:any,text:any):Observable<any>{
    return this.http.post<any>(`${API}send`,{
      userId,interlocutorId,text
    });
  }
}
