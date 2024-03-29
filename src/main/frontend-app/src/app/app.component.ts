import { Component } from '@angular/core';
import {TokenStorageService} from "./services/token-storage.service";
import {ToastrService} from "ngx-toastr";
import {MessengerService} from "./services/messenger.service";
import {MessengerWindowComponent} from "./components/messenger-window/messenger-window.component";
import {MatDialog} from "@angular/material/dialog";
import {OrganisationManagementComponent} from "./components/organisation-management/organisation-management.component";
import {ProfileManagementComponent} from "./components/profile-management/profile-management.component";
import {TaskService} from "./services/task.service";
import {TaskPreviewComponent} from "./components/tasks/task-preview/task-preview.component";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend-app';
  private roles: string[] = [];
  isLoggedIn = false;
  userId:any;
  isAdmin = false;
  login='';
  messengerInstantions:any;

  taskAssistant:any;

  constructor(private tokenStorageService:TokenStorageService,
              private toastr:ToastrService,
              private messengerService:MessengerService,
              private taskService:TaskService,
              public dialog: MatDialog,
              private router: Router) {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser()
      this.login = user.username;
      this.roles = user.roles;
      this.userId = user.id;
      if(this.roles.includes("ROLE_ADMIN"))
      {
        this.isAdmin = true;
      }
      this.messengerService.getAll(user.id).subscribe(res =>{
        this.messengerInstantions=res;
      },error => {
        this.toastr.error(error.error,"Błąd pobierania wiadomości!")
      });
      this.taskService.getWrapper(user.id).subscribe(res=>{
        this.taskAssistant=res;
      },error => {
        this.toastr.error(error.error,"Błąd!")
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
    if(instance?.interlocutor)
    {
      this.messengerService.setAsRead(this.userId,instance?.interlocutor.id).subscribe();
    }
    const modalRef = this.dialog.open(MessengerWindowComponent, {
      autoFocus: false,
      data:{messengerInstance:instance,userId:this.userId},
      width:"250px",
      height:"400px"
    });
    /*modalRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });*/
  }

  openOrgManager() {
    const modalRef = this.dialog.open(OrganisationManagementComponent, {
      disableClose: true,
      data:{userId:this.userId},
      hasBackdrop: true
    });
    modalRef.afterClosed().subscribe();
  }

  openProfileManager() {
    const modalRef = this.dialog.open(ProfileManagementComponent, {
      disableClose: true,
      data:{userId:this.userId},
      hasBackdrop: true
    });
    modalRef.afterClosed().subscribe();
  }

  openTaskPreview() {
    const modalRef = this.dialog.open(TaskPreviewComponent, {
      disableClose: true,
      data:{task:this.taskAssistant.task},
      hasBackdrop: true,
      panelClass: 'my-dialog',
      minWidth:"600px"
    });
    modalRef.afterClosed().subscribe(()=>{
      this.refresh();
    });
  }
  refresh(): void {
    window.location.reload();
  }

  openUserManager() {
    this.router.navigate(['/users']);
  }
}
