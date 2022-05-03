import { Component, OnInit } from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {TokenStorageService} from "../../services/token-storage.service";
import {DatePipe} from "@angular/common";
import * as moment from "moment";
import {TaskService} from "../../services/task.service";
import {ErrorMessageClass} from "../projects/projects/projects.component";
import {MatTabChangeEvent} from "@angular/material/tabs";
import {TaskPreviewComponent} from "../tasks/task-preview/task-preview.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  dzienTygodnia:any;
  dzisiejszaData:any;
  user:any;
  wrapper:any;
  constructor(private toastr:ToastrService,
              private tokenStorageService:TokenStorageService,
              public datepipe: DatePipe,
              private tasksService:TaskService,
              public dialog: MatDialog) {
    this.user = this.tokenStorageService.getUser();
  }

  ngOnInit(): void
  {
    this.dzienTygodnia = moment().locale("pl").format('dddd');
    this.dzisiejszaData =this.datepipe.transform((new Date), 'dd/MM/yyyy');
    this.getData("day");
  }

  getData(selectedMode:any):void{
    if(selectedMode){
      this.tasksService.getMyTasksWrapper(this.user.id,selectedMode).subscribe(res=>{
        this.wrapper=res;
      },error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
      });
    }
  }

  onTabChanged(event: MatTabChangeEvent) {
    let mode="day";
    switch (event.index){
      case 0:
      default:
        mode="day";
        break;
      case 1:
        mode="week";
        break;
      case 2:
        mode="month";
        break;
    }
    this.getData(mode);
  }

  openPreview(task: any) {
    const modalRef = this.dialog.open(TaskPreviewComponent, {
      disableClose: true,
      data:{task:task},
      hasBackdrop: true,
      panelClass: 'my-dialog',
      minWidth:"600px"
    });
    modalRef.afterClosed().subscribe(()=>{
      this.refresh();
    });
  }

  refresh(): void {
    window.location.reload();
  }
}
