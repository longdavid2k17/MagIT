import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const API = 'http://localhost:8080/api/tasks/';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http:HttpClient) { }

  getByTeamIdPageable(orgId:any,params:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}pageable/team/${orgId}`,{
      params
    });
  }

  getByProjectIdPageable(orgId:any,params:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}pageable/project/${orgId}`,{
      params
    });
  }

  getByOrganisationIdPageable(orgId:any,params:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}pageable/organisation/${orgId}`,{
      params
    });
  }

  getSubtasks(parentTaskId:any):Observable<any[]>{
    return this.http.get<any[]>(`${API}subtasks/${parentTaskId}`);
  }

  save(task:any):Observable<any> {
    return this.http.post<any>(`${API}save`,
      task);
  }

  saveSubtasks(tasks:any[]):Observable<any> {
    return this.http.post<any>(`${API}save-subtasks`,
      tasks);
  }

  delete(id:any):Observable<any> {
    return this.http.delete<any>(`${API}delete/${id}`);
  }
}
