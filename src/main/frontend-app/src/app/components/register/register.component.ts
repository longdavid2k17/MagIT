import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form: any = {
    name: null,
    surname: null,
    email: null,
    username: null,
    password: null,
    rePassword: null,
    inviteCode: null,
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService,private toastr:ToastrService) { }

  ngOnInit(): void {
  }
  onSubmit(): void {
    const { name, surname,email,username, password,rePassword,inviteCode  } = this.form;

    if(rePassword==password)
    {
      this.authService.registerUser(username, email, password, name, surname).subscribe(
        data => {
          console.log(data);
          this.isSuccessful = true;
          this.isSignUpFailed = false;
          this.toastr.success('Zarejestrowano użytkownika!');
        },
        err => {
          this.errorMessage = err.error.message;
          this.isSignUpFailed = true;
        }
      );
    }
    else
    {
      this.errorMessage = "Wprowadzone hasła nie są identyczne!"
      this.isSignUpFailed = true;
    }
  }
}
