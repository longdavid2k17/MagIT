import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OrganisationRolesService} from "../../../services/organisation-roles.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../../../services/user.service";
import {ProjectsService} from "../../../services/projects.service";
import {ErrorMessageClass} from "../../projects/projects/projects.component";
import {MatSelectChange} from "@angular/material/select";
import {TeamsService} from "../../../services/teams.service";

@Component({
  selector: 'app-team-form',
  templateUrl: './team-form.component.html',
  styleUrls: ['./team-form.component.css']
})
export class TeamFormComponent implements OnInit {
  isLinear = true;
  form: FormGroup;
  projects:any[]=[];
  users:any[]=[];
  roleUsers:any[]=[];
  roles:any[]=[];
  organisationId:any;

  userValue:any;
  roleValue:any;

  teamMembers:any[]=[];

  constructor(public dialogRef: MatDialogRef<TeamFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private rolesService:OrganisationRolesService,
              private fb:FormBuilder,
              private toastr:ToastrService,
              private userService:UserService,
              private projectsService:ProjectsService,
              private teamsService:TeamsService) {
    this.organisationId = data.organisationId;
    this.form = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null],
      defaultProject:[null],
      teamLeader:[null],
      organisationId:[null]
    });
    if(this.organisationId){
      this.userService.getByOrganisationIdNoPage(this.organisationId).subscribe(res=>{
        this.users=res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
      this.projectsService.getAllProjectsForOrg(this.organisationId).subscribe(res=>{
        this.projects=res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
      this.rolesService.getByOrganisationIdToList(this.organisationId).subscribe(res=>{
        this.roles=res;
      });
    }

  }

  ngOnInit(): void {
  }

  onSubmit() {
    let form = this.form.value;
    form.organisationId = this.organisationId;
    this.teamsService.save(form,this.teamMembers).subscribe(res=>{
      this.dialogRef.close("success");
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas zapisywania zespołu")
    });
  }

  onRoleChange(event: MatSelectChange) {
    if(event.value){
      this.userService.getByOrganisationIdAndRoleIdNoPage(this.organisationId,event.value.id).subscribe(res=>{
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
        user:this.userValue,
        role:this.roleValue
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
}
