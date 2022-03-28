import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {OrganisationRolesService} from "../../../services/organisation-roles.service";

@Component({
  selector: 'app-role-form',
  templateUrl: './role-form.component.html',
  styleUrls: ['./role-form.component.css']
})
export class RoleFormComponent implements OnInit {
  form: FormGroup;
  editMode = false;
  editedRole:any;
  organisationId:any;

  constructor(public dialogRef: MatDialogRef<RoleFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastr:ToastrService,
              private roleService:OrganisationRolesService,
              private fb: FormBuilder) {
    dialogRef.disableClose = true;
    this.form = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      iconName: [null, [Validators.required, Validators.minLength(3)]],
      id: [null],
      organisationId: [null],
    });
  }

  ngOnInit(): void {
    if(this.data?.organisationId)
    {
      this.organisationId = this.data.organisationId;
      this.form.patchValue({
        organisationId:this.organisationId
      });
    }
    if(this.data?.role)
    {
      this.editMode=true;
      this.editedRole = this.data.role;
      this.form.patchValue(this.editedRole);
    }
  }

  onSubmit() {
    if (!this.form.valid) {
      return;
    }
    this.roleService.save(this.form.value).subscribe(res=>{
      this.toastr.success("Poprawnie zapisano dane!","Sukces!");
      this.dialogRef.close();
    },error => {
      this.toastr.error(error.errorMessage,"Błąd podczas zapisywania danych!");
    })
  }
}
