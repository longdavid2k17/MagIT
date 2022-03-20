import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {TokenStorageService} from "../services/token-storage.service";

@Injectable()
export class UserEmulationInterceptor implements HttpInterceptor {

  private readonly token: string;

  constructor(private tokenStorageService:TokenStorageService) {
    const user = this.tokenStorageService.getUser();
    this.token = user.accessToken;
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if(this.token)
    {
      const modReq = request.clone({
        setHeaders: {
          'Authorization': `Bearer ${this.token}`
        }
      });
      return next.handle(modReq);
    }
    console.log("Zwracam zwykły request");
    return next.handle(request);
  }
}
