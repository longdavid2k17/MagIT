import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

const AUTH_API = 'http://localhost:8080/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'sign-in', {
      username,
      password
    }, httpOptions);
  }

  register(username: string, email: string, password: string,name: string,surname: string): Observable<any> {
    return this.http.post(AUTH_API + 'sign-up', {
      username,
      email,
      password,
      name,
      surname
    }, httpOptions);
  }

  verifyToken(token:any): Observable<any> {
    return this.http.post(AUTH_API + 'token/verify', token, {
      headers: new HttpHeaders({ 'Content-Type': 'text/plain' })
    });
  }

  resendToken(token:any): Observable<any> {
    return this.http.post(AUTH_API + 'token/resend', token, {
      headers: new HttpHeaders({ 'Content-Type': 'text/plain' })
    });
  }
}
