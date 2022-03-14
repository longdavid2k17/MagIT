import { Component, OnInit } from '@angular/core';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  isVisible = false;

  constructor(private toastr:ToastrService) { }

  ngOnInit(): void {
  }

  showContacts() {
    if(this.isVisible)
    {
      this.isVisible = false;
      this.toastr.success("Schowano");
    }
    else {
      this.isVisible = true;
      this.toastr.success("Pokazano");
    }

  }
}
