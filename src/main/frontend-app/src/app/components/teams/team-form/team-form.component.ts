import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OrganisationRolesService} from "../../../services/organisation-roles.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-team-form',
  templateUrl: './team-form.component.html',
  styleUrls: ['./team-form.component.css']
})
export class TeamFormComponent implements OnInit {
  form: FormGroup;
  constructor(public dialogRef: MatDialogRef<TeamFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private rolesService:OrganisationRolesService,
              private fb:FormBuilder,
              private toastr:ToastrService) {
    this.form = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null],
    });
  }

  ngOnInit(): void {
  }

  onSubmit() {

  }

}
