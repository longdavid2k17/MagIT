import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

const API = 'http://localhost:8080/api/files';

@Injectable({
  providedIn: 'root'
})
export class ResultFileUploadService {


  constructor(private http: HttpClient) { }

  upload(file: File,taskId:any): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const req = new HttpRequest('POST', `${API}/upload-result/${taskId}`, formData, {
      reportProgress: true,
      responseType: 'json'
    });
    return this.http.request(req);
  }

  getFiles(taskId:any): Observable<any> {
    return this.http.get(`${API}/result-files/${taskId}`);
  }
}
