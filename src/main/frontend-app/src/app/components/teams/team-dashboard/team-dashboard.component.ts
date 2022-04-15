import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../services/user.service";
import {TokenStorageService} from "../../../services/token-storage.service";
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {TeamsService} from "../../../services/teams.service";
import {OrganisationRolesService} from "../../../services/organisation-roles.service";
import {RoleFormComponent} from "../role-form/role-form.component";
import {OrganisationRoleChooserComponent} from "../../organisation-role-chooser/organisation-role-chooser.component";
import {PageEvent} from "@angular/material/paginator";
import {TeamFormComponent} from "../team-form/team-form.component";

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
  totalRolesElements: number = 0;
  totalUsersElements: number = 0;
  totalTeamsElements: number = 0;
  user:any;

  constructor(private userService:UserService,
              private teamsService:TeamsService,
              private roleService:OrganisationRolesService,
              private tokenStorageService:TokenStorageService,
              private toastr:ToastrService,
              public dialog: MatDialog) { }

  ngOnInit(): void {
    this.user = this.tokenStorageService.getUser();
    if(this.user?.organisation)
    {
      this.organisation = this.user.organisation;

      const usersRequest = {};
      // @ts-ignore
      usersRequest['page'] = 0;
      // @ts-ignore
      usersRequest['size'] = 10;
      this.getUsersData(this.user,usersRequest);


      const teamsRequest = {};
      // @ts-ignore
      teamsRequest['page'] = 0;
      // @ts-ignore
      teamsRequest['size'] = 10;
      this.getTeams(this.organisation.id,teamsRequest);

      const roleRequest = {};
      // @ts-ignore
      roleRequest['page'] = 0;
      // @ts-ignore
      roleRequest['size'] = 10;
      this.getRolesData(this.user,roleRequest);

    }
  }

  openRoleForm() {
    const modalRef = this.dialog.open(RoleFormComponent, {
      disableClose: true,
      data:{organisationId:this.organisation.id},
      hasBackdrop: true
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
      hasBackdrop: true
    });
    modalRef.afterClosed().subscribe(res => {
      this.refresh();
    });
  }

  refresh(): void {
    window.location.reload();
  }

  openRoleChooser(user: any)
  {
    const modalRef = this.dialog.open(OrganisationRoleChooserComponent, {
      disableClose: true,
      data:{user:user},
      hasBackdrop: true
    });
    modalRef.afterClosed().subscribe(res => {
      this.refresh();
    });
  }

  nextRolesPage(event: PageEvent) {
    const request = {};
    // @ts-ignore
    request['page'] = event.pageIndex.toString();
    // @ts-ignore
    request['size'] = event.pageSize.toString();
    this.getRolesData(this.user, request);
  }

  private getRolesData(user:any,request: any) {
    this.roleService.getByOrganisationId(user.organisation.id,request).subscribe(res=>{
      // @ts-ignore
      this.roles = res['content'];
      // @ts-ignore
      this.totalRolesElements = res['totalElements'];
    },error => {
      this.toastr.error(error.error,"Błąd pobierania ról!")
    });
  }

  private getUsersData(user:any,request: any) {
    this.userService.getByOrganisationId(user.organisation.id,request).subscribe(res=>{
      // @ts-ignore
      this.users = res['content'];
      // @ts-ignore
      this.totalUsersElements = res['totalElements'];
    },error => {
      this.toastr.error(error.error,"Błąd pobierania użytkowników!")
    });
  }

  private getTeams(organisationId:any,request: any) {
    this.teamsService.getByOrganisationId(organisationId,request).subscribe(res=>{
      // @ts-ignore
      this.teams = res['content'];
      // @ts-ignore
      this.totalTeamsElements = res['totalElements'];
    },error => {
      this.toastr.error(error.error,"Błąd pobierania zespołów!")
    });
  }

  nextUsersPage(event: PageEvent) {
    const request = {};
    // @ts-ignore
    request['page'] = event.pageIndex.toString();
    // @ts-ignore
    request['size'] = event.pageSize.toString();
    this.getUsersData(this.user, request);
  }


  openTeamForm() {
    const modalRef = this.dialog.open(TeamFormComponent, {
      disableClose: true,
      data:{organisationId:this.organisation.id},
      hasBackdrop: true,
      panelClass: 'my-dialog',
    });
    modalRef.afterClosed().subscribe(res =>{
      this.refresh();
    });
  }

  nextTeamsPage(event: PageEvent) {
    const request = {};
    // @ts-ignore
    request['page'] = event.pageIndex.toString();
    // @ts-ignore
    request['size'] = event.pageSize.toString();
    this.getTeams(this.user.organisation.id, request);
  }
}
