import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {ToastrService} from "ngx-toastr";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {MyErrorStateMatcher} from "../reset-password-confirmation/reset-password-confirmation.component";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form: FormGroup;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  matcher = new MyErrorStateMatcher();

  constructor(private authService: AuthService,
              private toastr:ToastrService,
              private fb: FormBuilder) {
    this.form = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      surname: [null, [Validators.required, Validators.minLength(3)]],
      email: [null, [Validators.required, Validators.minLength(3)]],
      username: [null, [Validators.required, Validators.minLength(3)]],
      password: [null, [Validators.required, Validators.minLength(3)]],
      rePassword: [null, [Validators.required, Validators.minLength(3)]],
      inviteCode: [null],
    }, { validators: this.checkPasswords });
  }

  ngOnInit(): void {
  }
  onSubmit(): void {
    if (!this.form.valid) {
      return;
    }
      this.authService.registerUser(this.form.value).subscribe(
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

  checkPasswords: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => {
    // @ts-ignore
    let pass = group.get('password').value;
    // @ts-ignore
    let confirmPass = group.get('rePassword').value
    return pass === confirmPass ? null : { notSame: true }
  }
}
