import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {TaskService} from "../../../services/task.service";
import {ErrorMessageClass} from "../../projects/projects/projects.component";

@Component({
  selector: 'app-task-preview',
  templateUrl: './task-preview.component.html',
  styleUrls: ['./task-preview.component.css']
})
export class TaskPreviewComponent implements OnInit {

  task:any;
  subtasks:any[]=[];
  taskResults:any[]=[];

  constructor(public dialogRef: MatDialogRef<TaskPreviewComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastr:ToastrService,
              private tasksService:TaskService) {
    this.task = data.task;
    this.tasksService.getSubtasks(this.task.id).subscribe(res=>{
      this.subtasks = res;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    })
  }

  ngOnInit(): void {
  }

  setTaskAsCompleted() {

  }

  /**
   * handle file from browsing
   */
  fileBrowseHandler(event:any) {
    console.log(event.files);
  }
}
