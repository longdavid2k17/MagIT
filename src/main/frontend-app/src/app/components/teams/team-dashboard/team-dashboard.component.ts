import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../services/user.service";
import {TokenStorageService} from "../../../services/token-storage.service";
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {TeamsService} from "../../../services/teams.service";

@Component({
  selector: 'app-team-dashboard',
  templateUrl: './team-dashboard.component.html',
  styleUrls: ['./team-dashboard.component.css']
})
export class TeamDashboardComponent implements OnInit {

  users:any[] = [];
  teams:any[] = [];

  constructor(private userService:UserService,
              private teamsService:TeamsService,
              private tokenStorageService:TokenStorageService,
              private toastr:ToastrService,
              public dialog: MatDialog) { }

  ngOnInit(): void {
    const user = this.tokenStorageService.getUser();
    if(user?.organisation)
    {
      this.userService.getByOrganisationId(user.organisation.id).subscribe(res=>{
        this.users=res;
      },error => {
        this.toastr.error(error.error,"Błąd pobierania użytkowników!")
      });
      this.teamsService.getByOrganisationId(user.organisation.id).subscribe(res=>{
        this.teams=res;
      },error => {
        this.toastr.error(error.error,"Błąd pobierania zespołów!")
      })
    }
  }

}
