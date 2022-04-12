import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OrganisationRolesService} from "../../../services/organisation-roles.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../../../services/user.service";
import {ProjectsService} from "../../../services/projects.service";
import {ErrorMessageClass} from "../../projects/projects/projects.component";

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
  organisationId:any;
  constructor(public dialogRef: MatDialogRef<TeamFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private rolesService:OrganisationRolesService,
              private fb:FormBuilder,
              private toastr:ToastrService,
              private userService:UserService,
              private projectsService:ProjectsService) {
    this.organisationId = data.organisationId;
    this.form = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null],
      defaultProject:[null],
      teamLeader:[null]
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
    }

  }

  ngOnInit(): void {
  }

  onSubmit() {

  }

}
