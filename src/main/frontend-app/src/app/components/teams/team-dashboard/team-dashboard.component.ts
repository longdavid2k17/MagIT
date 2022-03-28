import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../services/user.service";
import {TokenStorageService} from "../../../services/token-storage.service";
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {TeamsService} from "../../../services/teams.service";
import {OrganisationRolesService} from "../../../services/organisation-roles.service";
import {RoleFormComponent} from "../role-form/role-form.component";

@Component({
  selector: 'app-team-dashboard',
  templateUrl: './team-dashboard.component.html',
  styleUrls: ['./team-dashboard.component.css']
})
export class TeamDashboardComponent implements OnInit {

  users:any[] = [];
  teams:any[] = [];
  roles:any[] = [];
  organisation:any;

  constructor(private userService:UserService,
              private teamsService:TeamsService,
              private roleService:OrganisationRolesService,
              private tokenStorageService:TokenStorageService,
              private toastr:ToastrService,
              public dialog: MatDialog) { }

  ngOnInit(): void {
    const user = this.tokenStorageService.getUser();
    if(user?.organisation)
    {
      this.organisation = user.organisation;
      this.userService.getByOrganisationId(user.organisation.id).subscribe(res=>{
        this.users=res;
      },error => {
        this.toastr.error(error.error,"Błąd pobierania użytkowników!")
      });
      this.teamsService.getByOrganisationId(user.organisation.id).subscribe(res=>{
        this.teams=res;
      },error => {
        this.toastr.error(error.error,"Błąd pobierania zespołów!")
      });
      this.roleService.getByOrganisationId(user.organisation.id).subscribe(res=>{
        this.roles=res;
      },error => {
        this.toastr.error(error.error,"Błąd pobierania ról!")
      });
    }
  }

  openRoleForm() {
    const modalRef = this.dialog.open(RoleFormComponent, {
      disableClose: true,
      data:{organisationId:this.organisation.id},
    });
    modalRef.afterClosed().subscribe(res =>{
      this.refresh();
    });
  }

  editRoleForm(role: any)
  {
    const modalRef = this.dialog.open(RoleFormComponent, {
      disableClose: true,
      data:{role:role},
    });
    modalRef.afterClosed().subscribe(res => {
      this.refresh();
    });
  }

  refresh(): void {
    window.location.reload();
  }
}
