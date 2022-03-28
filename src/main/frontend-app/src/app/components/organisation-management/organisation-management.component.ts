import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {OrganisationService} from "../../services/organisation.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-organisation-management',
  templateUrl: './organisation-management.component.html',
  styleUrls: ['./organisation-management.component.css']
})
export class OrganisationManagementComponent implements OnInit {
  form: FormGroup;
  dataExist = false;
  inviteCode = '';

  constructor(public dialogRef: MatDialogRef<OrganisationManagementComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastr:ToastrService,
              private organisationService:OrganisationService,
              private fb: FormBuilder) {
    dialogRef.disableClose = true;
    this.form = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null, [Validators.required, Validators.minLength(3)]],
      inviteCode: [null],
      creationDate: [null],
      id: [null],
      ownerId: [null],
    });
  }

  ngOnInit(): void {

    if(this.data?.userId)
    {
      this.organisationService.getByOwnerId(this.data.userId).subscribe(res=>{
        this.dataExist=true;
        this.inviteCode = res.inviteCode;
        this.form.patchValue(res);
      },error => {
        this.toastr.error(error.error,"Błąd podczas pobierania danych!");
        this.dataExist = false;
      })
    }
    else this.dataExist = false;
  }

  onSubmit() {
    if (!this.form.valid) {
      return;
    }
    this.organisationService.save(this.form.value).subscribe(res=>{
      this.toastr.success("Poprawnie zapisano dane!","Sukces!");
      this.dialogRef.close();
    },error => {
      this.toastr.error(error.errorMessage,"Błąd podczas zapisywania danych!");
    })
  }
}
