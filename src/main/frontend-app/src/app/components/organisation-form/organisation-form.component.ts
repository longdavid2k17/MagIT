import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {OrganisationService} from "../../services/organisation.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TokenStorageService} from "../../services/token-storage.service";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-organisation-form',
  templateUrl: './organisation-form.component.html',
  styleUrls: ['./organisation-form.component.css']
})
export class OrganisationFormComponent implements OnInit {
  form: FormGroup;
  login ='';
  ownerId:any;

  constructor(public dialogRef: MatDialogRef<OrganisationFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastr:ToastrService,
              private authService:AuthService,
              private fb: FormBuilder,
              private tokenStorage: TokenStorageService,) {
    this.form = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null],
    });
    this.login = data.login;
  }

  ngOnInit(): void {
  }

  onSubmit() {
    if (!this.form.valid) {
      return;
    }
    if (this.tokenStorage.getToken()) {
      this.ownerId = this.tokenStorage.getUser().id;
    }
    if(this.ownerId) {
      this.form.patchValue(this.ownerId);
    }
    this.authService.createOrganisationFromForm(this.form.value,this.login).subscribe(res=>{
      this.toastr.success("Poprawnie zapisano dane!","Sukces!");
      this.dialogRef.close();
    },error => {
      this.toastr.error(error.errorMessage,"Błąd podczas zapisywania danych!");
    })
  }

}
