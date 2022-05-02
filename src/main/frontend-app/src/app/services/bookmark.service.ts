import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const API = 'http://localhost:8080/api/solutions/';

@Injectable({
  providedIn: 'root'
})
export class BookmarkService {

  constructor(private http:HttpClient) { }

  getByOrganisation(id:any):Observable<any>{
    return this.http.get<any>(`${API}organisation/${id}`);
  }

  save(bookmark:any):Observable<any>
  {
    return this.http.post<any>(`${API}save`,
      bookmark);
  }

  delete(taskId:any):Observable<any>
  {
    return this.http.delete<any>(`${API}delete/${taskId}`);
  }

  getAllTypes(id: any):Observable<any>{
    return this.http.get<any>(`${API}types/${id}`);
  }
}
