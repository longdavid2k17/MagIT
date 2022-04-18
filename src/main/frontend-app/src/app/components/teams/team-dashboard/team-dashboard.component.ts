import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {UserService} from "../../../services/user.service";
import {TokenStorageService} from "../../../services/token-storage.service";
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {TeamsService} from "../../../services/teams.service";
import {OrganisationRolesService} from "../../../services/organisation-roles.service";
import {RoleFormComponent} from "../role-form/role-form.component";
import {OrganisationRoleChooserComponent} from "../../organisation-role-chooser/organisation-role-chooser.component";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {TeamFormComponent} from "../team-form/team-form.component";
import {MatTableDataSource} from "@angular/material/table";
import {
  ConfirmationDialogComponent,
  ConfirmDialogModel
} from "../../general/confirmation-dialog/confirmation-dialog.component";
import {ErrorMessageClass} from "../../projects/projects/projects.component";
import {TeamEditFormComponent} from "../team-edit-form/team-edit-form.component";
import {ManageTeamMembershipComponent} from "../manage-team-membership/manage-team-membership.component";

@Component({
  selector: 'app-team-dashboard',
  templateUrl: './team-dashboard.component.html',
  styleUrls: ['./team-dashboard.component.css']
})
export class TeamDashboardComponent implements OnInit,AfterViewInit {
  displayedColumns: string[] = ['action','name', 'defaultProject','teamLeader','teamTasks', 'individualTasks'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatPaginator) paginator: MatPaginator;
  isLoading:boolean=false;

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

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
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
    this.isLoading=true;
    this.teamsService.getByOrganisationId(organisationId,request).subscribe(res=>{
      // @ts-ignore
      this.teams = res['content'];
      // @ts-ignore
      this.totalTeamsElements = res['totalElements'];
      // @ts-ignore
      this.dataSource = new MatTableDataSource(res['content']);
      this.dataSource.paginator = this.paginator;
    },error => {
      this.toastr.error(error.error,"Błąd pobierania zespołów!")
    });
    this.isLoading=false;
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

  deleteTeam(row: any) {
    const message = `Czy jesteś pewny że chcesz usunąć ten zespół?`;
    const dialogData = new ConfirmDialogModel("Wymagane potwierdzenie", message);
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      maxWidth: "400px",
      data: dialogData,
      hasBackdrop: true,
      disableClose:true
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if(dialogResult)
      {
        this.teamsService.deleteTeam(row.id).subscribe(res=>{
          this.toastr.success("Usunięto projekt!")
        },error => {
          this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!");
        });
      }
    });
  }

  editTeam(row: any) {
    const modalRef = this.dialog.open(TeamEditFormComponent, {
      disableClose: true,
      data:{team:row},
      hasBackdrop: true,
      panelClass: 'my-dialog',
    });
    modalRef.afterClosed().subscribe(res =>{
      this.refresh();
    });
  }

  openTeamManager(user: any) {
    const modalRef = this.dialog.open(ManageTeamMembershipComponent, {
      disableClose: true,
      data:{user:user},
      hasBackdrop: true,
      panelClass: 'my-dialog',
    });
    modalRef.afterClosed().subscribe(res =>{
      this.refresh();
    });
  }
}
