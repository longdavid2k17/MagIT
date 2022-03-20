import { Component, OnInit } from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {TokenStorageService} from "../../services/token-storage.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private toastr:ToastrService,private tokenStorageService:TokenStorageService) { }

  ngOnInit(): void {
  }



  reloadPage(): void {
    window.location.reload();
  }
}
