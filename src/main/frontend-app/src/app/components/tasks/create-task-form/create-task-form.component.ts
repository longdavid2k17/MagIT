import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {ProjectsService} from "../../../services/projects.service";
import {UserService} from "../../../services/user.service";
import {ErrorMessageClass} from "../../projects/projects/projects.component";
import {TeamsService} from "../../../services/teams.service";
import {MatSelectChange} from "@angular/material/select";

@Component({
  selector: 'app-create-task-form',
  templateUrl: './create-task-form.component.html',
  styleUrls: ['./create-task-form.component.css']
})
export class CreateTaskFormComponent implements OnInit {

  @ViewChild('inputElement') input :ElementRef<HTMLInputElement>;
  organisationId:any;
  form: FormGroup;
  projects:any[] =[];
  users:any[] =[];
  pmUsers:any[] =[];
  teams:any[] =[];
  subTasks:any[]=[];
  isLinear = true;

  subtaskValue:any;
  userValue:any;

  constructor(public dialogRef: MatDialogRef<CreateTaskFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastr:ToastrService,
              private projectService:ProjectsService,
              private userService:UserService,
              private fb: FormBuilder,
              private projectsService:ProjectsService,
              private teamsService:TeamsService) {
    this.organisationId = data.organisationId;
    this.form = this.fb.group({
      title: [null, [Validators.required, Validators.minLength(3)]],
      description: [null],
      deleted: [false],
      completed: [false],
      team: [null],
      organisation: [null],
      project:[null],
      user: [null],
      parentTask: [null],
      creationDate: [null],
      modificationDate: [null],
      startDate: [null],
      deadlineDate: [null],
      gitHubUrl: [null],
      status: [null],
      id: [null],
    });
  }

  ngOnInit(): void {
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
      this.teamsService.getByOrganisationIdNoPage(this.organisationId).subscribe(res=>{
        this.teams=res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
    }

  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }
    let form = this.form.value;
    form.organisationId = this.organisationId;
  }

  compareFn(c1: any, c2: any): boolean {
    return c1 && c2 ? c1.id === c2.id : c1 === c2;
  }

  remove(subTask: any) {
    this.subTasks.forEach((value,index)=>{
      if(value==subTask) this.subTasks.splice(index,1);
    });
  }

  addSubtask() {
    if(this.subtaskValue && this.userValue){
      let subtask ={
        user:this.userValue,
        title:this.subtaskValue
      }
      this.subTasks.push(subtask);
      this.subtaskValue='';
    }
  }



  onUserChange(event: MatSelectChange) {
    this.userValue = event.value;
  }

  onTitleChange(target: any) {
    this.subtaskValue = target.value;
  }
}
