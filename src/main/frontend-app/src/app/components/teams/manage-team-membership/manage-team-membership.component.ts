import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OrganisationRolesService} from "../../../services/organisation-roles.service";
import {TeamsService} from "../../../services/teams.service";
import {ErrorMessageClass} from "../../projects/projects/projects.component";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-manage-team-membership',
  templateUrl: './manage-team-membership.component.html',
  styleUrls: ['./manage-team-membership.component.css']
})
export class ManageTeamMembershipComponent implements OnInit {

  availableTeams:any[]=[];
  selectedTeams:any[]=[];
  organisationId:any;
  user:any;

  constructor(public dialogRef: MatDialogRef<ManageTeamMembershipComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private rolesService:OrganisationRolesService,
              private teamService:TeamsService,
              private toastr:ToastrService,) {
    this.user = data.user;
    this.organisationId = data.user.organisation.id;
  }

  ngOnInit(): void {
    this.teamService.getByOrganisationIdNoPage(this.organisationId).subscribe(res=>{
      this.availableTeams=res;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!");
    });
    this.teamService.getAllUserTeams(this.user.id).subscribe(res=>{
      this.selectedTeams=res;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!");
    });
  }

  setSelection(team: any) {
    if(this.selectedTeams.includes(team))
      this.selectedTeams.concat(team,1);
    else this.selectedTeams.push(team);
  }

  save() {

  }
}
