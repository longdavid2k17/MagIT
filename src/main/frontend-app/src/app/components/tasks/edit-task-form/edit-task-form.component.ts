import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {ProjectsService} from "../../../services/projects.service";
import {UserService} from "../../../services/user.service";
import {TeamsService} from "../../../services/teams.service";
import {TaskService} from "../../../services/task.service";
import {ErrorMessageClass} from "../../projects/projects/projects.component";
import {MatSelectChange} from "@angular/material/select";

@Component({
  selector: 'app-edit-task-form',
  templateUrl: './edit-task-form.component.html',
  styleUrls: ['./edit-task-form.component.css']
})
export class EditTaskFormComponent implements OnInit {
  task:any;
  organisation:any;
  form: FormGroup;
  projects:any[] =[];
  users:any[] =[];
  teams:any[] =[];
  subTasks:any[]=[];
  isLinear = true;

  subtaskValue:any;
  userValue:any;

  constructor(public dialogRef: MatDialogRef<EditTaskFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastr:ToastrService,
              private projectService:ProjectsService,
              private userService:UserService,
              private fb: FormBuilder,
              private projectsService:ProjectsService,
              private teamsService:TeamsService,
              private taskService:TaskService) {
    this.organisation = data.organisation;
    this.task = data.task;
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
      startTime: [null],
      deadlineDate: [null],
      deadlineTime: [null],
      gitHubUrl: [null],
      status: [null],
      id: [null],
    });
    this.taskService.getSubtasks(this.task.id).subscribe(res=>{
      this.subTasks=res;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
    this.form.patchValue(this.task);
    let startTime = null;
    let deadlineTime = null;
    if(this.task?.startDate){
      const date = new Date(this.task.startDate);
      startTime = date.getHours()+":"+date.getMinutes();
      this.form.controls['startTime'].setValue(startTime);
    }
    if(this.task?.deadlineDate){
      const date = new Date(this.task.deadlineDate);
      deadlineTime = date.getHours()+":"+date.getMinutes();
      this.form.controls['deadlineTime'].setValue(deadlineTime);
    }
  }

  ngOnInit(): void {
    if(this.organisation?.id){
      this.userService.getByOrganisationIdNoPage(this.organisation.id).subscribe(res=>{
        this.users=res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
      this.projectsService.getAllProjectsForOrg(this.organisation.id).subscribe(res=>{
        this.projects=res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
      this.teamsService.getByOrganisationIdNoPage(this.organisation.id).subscribe(res=>{
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
    this.taskService.edit(form).subscribe(res=>{
      this.saveSubtasks(res);
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!");
    });
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
        id:null,
        organisation:this.organisation,
        project:null,
        team:null,
        parentTask:null,
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

  private saveSubtasks(parentTask: any)
  {
    if(this.subTasks.length>0){
      for(let i=0;i<this.subTasks.length;i++){
        if(!this.subTasks[i].parentTask){
          this.subTasks[i].parentTask = parentTask;
        }
      }
      this.taskService.editSubtasks(this.subTasks,parentTask.id).subscribe(res=>{
        this.dialogRef.close();
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!")
      });
    } else this.dialogRef.close("SUCCESS");
  }

  dismiss() {
    this.dialogRef.close();
  }
}
