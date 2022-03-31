import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import {TokenStorageService} from "../services/token-storage.service";

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(private tokenService:TokenStorageService) {
  }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.isAuthorized(route);
  }

  isAuthorized(route:ActivatedRouteSnapshot):boolean{
    const userRoles = this.tokenService.getUser().roles;
    const expectedRoles = route.data.expectedRoles;
    const roleMatches = userRoles.findIndex((role: any) => expectedRoles.indexOf(role) !== -1);
    return (roleMatches >= 0);
  }

}
