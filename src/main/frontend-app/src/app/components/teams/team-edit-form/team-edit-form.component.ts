import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OrganisationRolesService} from "../../../services/organisation-roles.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../../../services/user.service";
import {ProjectsService} from "../../../services/projects.service";
import {TeamsService} from "../../../services/teams.service";
import {MatSelectChange} from "@angular/material/select";
import {ErrorMessageClass} from "../../projects/projects/projects.component";

@Component({
  selector: 'app-team-edit-form',
  templateUrl: './team-edit-form.component.html',
  styleUrls: ['./team-edit-form.component.css']
})
export class TeamEditFormComponent implements OnInit {
  team:any;
  isLinear = true;
  form: FormGroup;
  projects:any[]=[];
  users:any[]=[];
  roleUsers:any[]=[];
  roles:any[]=[];

  userValue:any;
  roleValue:any;

  teamMembers:any[]=[];

  constructor(public dialogRef: MatDialogRef<TeamEditFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private rolesService:OrganisationRolesService,
              private fb:FormBuilder,
              private toastr:ToastrService,
              private userService:UserService,
              private projectsService:ProjectsService,
              private teamsService:TeamsService) {
    this.team = data.team;
    this.form = this.fb.group({
      id: [null,Validators.required],
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null],
      defaultProject:[null],
      teamLeader:[null],
      organisationId:[null]
    });

  }

  ngOnInit(): void {
    if(this.team?.organisationId){
      this.userService.getByOrganisationIdNoPage(this.team.organisationId).subscribe(res=>{
        this.users=res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
      this.projectsService.getAllProjectsForOrg(this.team.organisationId).subscribe(res=>{
        this.projects=res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
      this.rolesService.getByOrganisationIdToList(this.team.organisationId).subscribe(res=>{
        this.roles=res;
      });
    }
    if(this.team){
      this.form.patchValue(this.team);
      this.teamsService.getTeamMembersByTeamId(this.team.id).subscribe(res=>{
        this.teamMembers = res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania członków zespołu");
      })
    }
  }

  onSubmit() {
    let form = this.form.value;
    this.teamsService.edit(form,this.teamMembers).subscribe(res=>{
      this.dialogRef.close("success");
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas zapisywania zespołu")
    });
  }

  onRoleChange(event: MatSelectChange) {
    if(event.value){
      this.userService.getByOrganisationIdAndRoleIdNoPage(this.team.organisationId,event.value.id).subscribe(res=>{
        this.roleUsers=res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd pobierania danych!");
      });
      this.roleValue = event.value;
    }
    else this.roleUsers=[];
  }

  /*
  TODO załatać funkcje sprawdzania czy w tablicy jest już taki user, pozwala na dodanie usera po zmianie wartości na mat-select
   */
  addTeamMember() {
    if(this.roleValue && this.userValue){
      let teamMemeber ={
        id:null,
        user:this.userValue,
        role:this.roleValue,
        team:this.team
      }
      if(this.contains(teamMemeber)){
        this.toastr.warning("Zdefiniowałeś już tego członka zespołu!","Nie dodano pozycji")
      }
      else this.teamMembers.push(teamMemeber);
    }
  }

  onUserChange(event: MatSelectChange) {
    this.userValue = event.value;
  }

  remove(member: any) {
    this.teamMembers.forEach((value,index)=>{
      if(value==member) this.teamMembers.splice(index,1);
    });
  }

  contains(obj:any):boolean {
    if(this.teamMembers){
      var i = this.teamMembers.length;
      while (i--) {
        if (this.teamMembers[i].user === obj.user && this.teamMembers[i].role === obj.role) {
          return true;
        }
      }
      return false;
    }
    return false;
  }

  compareFn(c1: any, c2: any): boolean {
    return c1 && c2 ? c1.id === c2.id : c1 === c2;
  }
}
