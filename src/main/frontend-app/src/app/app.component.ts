import { Component } from '@angular/core';
import {TokenStorageService} from "./services/token-storage.service";
import {ToastrService} from "ngx-toastr";
import {MessengerService} from "./services/messenger.service";
import {MessengerWindowComponent} from "./components/messenger-window/messenger-window.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MatDialog} from "@angular/material/dialog";

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
  messengerInstantions:any;

  constructor(private tokenStorageService:TokenStorageService, private toastr:ToastrService,
              private messengerService:MessengerService,private modalService: NgbModal,
              public dialog: MatDialog) {
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
        this.messengerInstantions=res;
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

  openMessenger(instance: any)
  {
    const modalRef = this.dialog.open(MessengerWindowComponent, {
      autoFocus: false,
      data:{messengerInstance:instance},
      width:"250px",
      height:"400px"
    });
    modalRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
