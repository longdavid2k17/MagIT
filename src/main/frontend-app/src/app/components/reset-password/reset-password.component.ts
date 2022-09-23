import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {ToastrService} from "ngx-toastr";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  form: FormGroup;
  success = false;
  errorMessage = '';
  constructor(private authService:AuthService,
              private toastr:ToastrService,
              private fb: FormBuilder) {
    this.form = this.fb.group({
      email: [null, [Validators.required, Validators.minLength(3)]],
    });
  }

  ngOnInit(): void {
  }

  resetPassword():void {
    if (!this.form.valid) {
      return;
    }
    this.authService.resetPassword(this.form.value).subscribe(
      data => {
        this.success=true;
        this.toastr.success("Pomyślnie wysłano żądanie! Kliknij w przesłany link")
      },
      err => {
        this.errorMessage = err.error;
        this.toastr.error(this.errorMessage,'Błąd!');
      }
    );
  }
}
