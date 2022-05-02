import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {TaskService} from "../../../services/task.service";
import {ErrorMessageClass} from "../../projects/projects/projects.component";
import {TaskRealizationService} from "../../../services/task-realization.service";

@Component({
  selector: 'app-task-preview',
  templateUrl: './task-preview.component.html',
  styleUrls: ['./task-preview.component.css']
})
export class TaskPreviewComponent implements OnInit {

  canChangeRealization:boolean | null=false;
  status:boolean | null=false;

  task:any;
  subtasks:any[]=[];
  taskResults:any[]=[];

  constructor(public dialogRef: MatDialogRef<TaskPreviewComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastr:ToastrService,
              private tasksService:TaskService,
              private realizationService:TaskRealizationService) {
    this.task = data.task;
    this.getData();
  }

  getData():void{
    this.tasksService.getSubtasks(this.task.id).subscribe(res=>{
      this.subtasks = res;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
    this.realizationService.verifyRealizationPossibility(this.task.id).subscribe(res=>{
      this.canChangeRealization = res;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
    this.realizationService.getRealizationStatus(this.task.id).subscribe(res=>{
      this.status = res === "Brak realizacji";
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }

  ngOnInit(): void {
  }

  setTaskAsCompleted() {
    this.tasksService.setStatus(this.task.id,"WYKONANE").subscribe(()=>{
    this.toastr.success("Oznaczono zadanie jako wykonane!");
    this.task.completed = true;
    this.getData();
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }

  setTaskAsInRealization() {
    this.tasksService.setStatus(this.task.id,"REALIZACJA").subscribe(()=>{
      this.toastr.success("Oznaczono zadanie jako w realizacji!");
      this.task.completed = false;
      this.getData();
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }

  changeRealizationStatus() : void {
    this.realizationService.changeStatus(this.task.id).subscribe(()=>{
      if(this.status) this.toastr.success("Rozpoczęto rejestrację czasu!");
      else this.toastr.success("Zatrzymano rejestrację czasu!");
      this.status=!this.status;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }

  /**
   * handle file from browsing
   */
  fileBrowseHandler(event:any) {
    console.log(event.files);
  }

  setSubtaskCompletedValue(subtask: any,event:any) {
    let status = "";
    if(event.checked) status="WYKONANE";
    else status = "REALIZACJA";
    this.tasksService.setStatus(subtask?.id,status).subscribe(()=>{
      this.getData();
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }
}
