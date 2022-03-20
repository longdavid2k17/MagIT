import { Component, OnInit } from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {TokenStorageService} from "../../services/token-storage.service";
import {DatePipe} from "@angular/common";
import * as moment from "moment";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  dzienTygodnia:any;
  dzisiejszaData:any;
  constructor(private toastr:ToastrService,private tokenStorageService:TokenStorageService,public datepipe: DatePipe) { }

  ngOnInit(): void
  {
    this.dzienTygodnia = moment().locale("pl").format('dddd');
    this.dzisiejszaData =this.datepipe.transform((new Date), 'dd/MM/yyyy');
  }



  reloadPage(): void {
    window.location.reload();
  }
}
