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

  getWrapper(userId:any):Observable<any>{
    return this.http.get<any>(`${API}wrapper/${userId}`);
  }

  getMyTasksWrapper(userId:any,mode:any):Observable<any>{
    return this.http.get<any>(`${API}my-tasks/${userId}/${mode}`);
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

  setStatus(taskId:any,status:string):Observable<any>{
    return this.http.get<any>(`${API}set-status/${taskId}/${status}`);
  }

  save(task:any):Observable<any> {
    return this.http.post<any>(`${API}save`,
      task);
  }

  edit(task:any):Observable<any> {
    return this.http.post<any>(`${API}edit`,
      task);
  }

  saveSubtasks(tasks:any[]):Observable<any> {
    return this.http.post<any>(`${API}save-subtasks`,
      tasks);
  }

  editSubtasks(tasks:any[],parentTaskId:any):Observable<any> {
    return this.http.post<any>(`${API}edit-subtasks/${parentTaskId}`,
      tasks);
  }

  delete(id:any):Observable<any> {
    return this.http.delete<any>(`${API}delete/${id}`);
  }
}
