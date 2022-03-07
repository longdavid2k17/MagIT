import { Component, OnInit } from '@angular/core';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit {

  constructor(private toastr:ToastrService) { }

  ngOnInit(): void {
    this.toastr.success('Zalogowano!')
  }

}
