import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
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

  logUser(formVal:any): Observable<any> {
    return this.http.post(AUTH_API + 'sign-in', formVal, httpOptions);
  }

  registerUser(formVal: any): Observable<any> {
    return this.http.post<any>(AUTH_API + 'sign-up', formVal, httpOptions);
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

  resetPassword(formVal: any) : Observable<any> {
    return this.http.post(AUTH_API + 'reset-password', formVal.email, {
      headers: new HttpHeaders({ 'Content-Type': 'text/plain' })
    });
  }

  setNewPassword(formVal: any, token: string | null) {
    return this.http.post(AUTH_API + 'set-new-password', {
      password:formVal.password,
      token:token
    }, httpOptions);
  }

  createOrganisation(login: string, name: any, description: any) : Observable<any> {
    return this.http.post(AUTH_API + 'create-org', {
      login,
      name,
      description
    }, httpOptions);
  }

  createOrganisationFromForm(formValue: any,login:any) {
    return this.http.post(AUTH_API + 'create-org', {
      name:formValue.name,
      description:formValue.description,
      login:login
    }, httpOptions);
  }

  grantPmRole(userId:any) {
    return this.http.get(AUTH_API + `grant-pm/${userId}`);
  }

  removePmRole(userId:any) {
    return this.http.get(AUTH_API + `remove-pm/${userId}`);
  }
}
