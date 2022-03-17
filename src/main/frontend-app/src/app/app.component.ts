import { Component } from '@angular/core';
import {TokenStorageService} from "./services/token-storage.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend-app';
  private roles: string[] = [];
  isLoggedIn = false;
  login='';
  showAdminBoard = false;
  showModeratorBoard = false;

  constructor(private tokenStorageService:TokenStorageService, private toastr:ToastrService) {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.login = user.username;
      this.roles = user.roles;
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    window.location.reload();
    this.toastr.success("Wylogowano użytkownika!");
  }
}
