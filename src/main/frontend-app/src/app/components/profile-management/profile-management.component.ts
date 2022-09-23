import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-profile-management',
  templateUrl: './profile-management.component.html',
  styleUrls: ['./profile-management.component.css']
})
export class ProfileManagementComponent implements OnInit {
  form: FormGroup;
  dataExist = false;

  constructor(public dialogRef: MatDialogRef<ProfileManagementComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastr:ToastrService,
              private userService:UserService,
              private fb: FormBuilder) {
    dialogRef.disableClose = true;
    this.form = this.fb.group({
      id: [null],
      username: [null, [Validators.required, Validators.minLength(3)]],
      email: [null, [Validators.required, Validators.minLength(3)]],
      bio: [null],
      password: [null],
      name: [null],
      surname: [null],
      organisation: [null],
      organisationRoles:[null],
      enabled:[null],
      lastLogged:[null],
      roles:[null],
    });
  }

  ngOnInit(): void {
    if(this.data?.userId)
    {
      this.userService.getById(this.data.userId).subscribe(res=>{
        this.dataExist=true;
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
    this.userService.save(this.form.value).subscribe(res=>{
      this.toastr.success("Poprawnie zapisano dane!","Sukces!");
      this.dialogRef.close();
    },error => {
      this.toastr.error(error.errorMessage,"Błąd podczas zapisywania danych!");
    })
  }
}
