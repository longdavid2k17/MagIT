import { Component } from '@angular/core';
import {TokenStorageService} from "./services/token-storage.service";
import {ToastrService} from "ngx-toastr";
import {MessengerService} from "./services/messenger.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend-app';
  private roles: string[] = [];
  isLoggedIn = false;
  hasBeenOpened = false;
  isAdmin = false;
  login='';
  orgUsers:any[]=[]

  constructor(private tokenStorageService:TokenStorageService, private toastr:ToastrService, private messengerService:MessengerService) {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser()
      this.login = user.username;
      this.roles = user.roles;
      if(this.roles.includes("ROLE_ADMIN"))
      {
        this.isAdmin = true;
      }
      this.messengerService.getAll(user.id).subscribe(res =>{
        this.orgUsers=res;
      },error => {
        this.toastr.error(error.error,"Błąd pobierania wiadomości!")
      })
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    window.location.reload();
    this.toastr.success("Wylogowano użytkownika!");
  }
}
