import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  form: any = {
    email: null
  };
  success = false;
  errorMessage = '';
  constructor(private authService:AuthService,private toastr:ToastrService) { }

  ngOnInit(): void {
  }

  resetPassword() {
    const { email } = this.form;
    this.authService.resetPassword(email).subscribe(
      data => {
        this.success=true;
        this.toastr.success("Pomyślnie wysłano żądanie!")
      },
      err => {
        this.errorMessage = err.error;
        this.toastr.error(this.errorMessage,'Błąd!');
      }
    );
  }
}
