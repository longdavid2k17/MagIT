import { Component, OnInit } from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {TokenStorageService} from "../../services/token-storage.service";
import {DatePipe} from "@angular/common";
import * as moment from "moment";
import {TaskService} from "../../services/task.service";
import {ErrorMessageClass} from "../projects/projects/projects.component";

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
              private tasksService:TaskService) {
    this.user = this.tokenStorageService.getUser();
  }

  ngOnInit(): void
  {
    this.dzienTygodnia = moment().locale("pl").format('dddd');
    this.dzisiejszaData =this.datepipe.transform((new Date), 'dd/MM/yyyy');
    this.tasksService.getMyTasksWrapper(this.user.id).subscribe(res=>{
      this.wrapper=res;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    })
  }



  reloadPage(): void {
    window.location.reload();
  }
}
