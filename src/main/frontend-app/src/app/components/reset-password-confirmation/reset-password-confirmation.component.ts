import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {timeout} from "rxjs/operators";
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup, FormGroupDirective, NgForm,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {ErrorStateMatcher} from "@angular/material/core";

@Component({
  selector: 'app-reset-password-confirmation',
  templateUrl: './reset-password-confirmation.component.html',
  styleUrls: ['./reset-password-confirmation.component.css']
})
export class ResetPasswordConfirmationComponent implements OnInit {
  form: FormGroup;
  token: string | null = '';
  isValid=false;
  errorMessage = '';
  matcher = new MyErrorStateMatcher();

  constructor(private authService: AuthService,
              private route: ActivatedRoute,
              private toastr:ToastrService,
              private router: Router,
              private fb: FormBuilder) {
    this.form = this.fb.group({
      password: [null, [Validators.required, Validators.minLength(3)]],
      repassword:  ['']
    }, { validators: this.checkPasswords });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
    if(this.token)
    this.isValid=true;


  }

  checkPasswords: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => {
    // @ts-ignore
    let pass = group.get('password').value;
    // @ts-ignore
    let confirmPass = group.get('repassword').value
    return pass === confirmPass ? null : { notSame: true }
  }

  resetPassword():void {
    if (!this.form.valid) {
      return;
    }
      this.authService.setNewPassword(this.form.value,this.token).subscribe(response => {
        this.toastr.success("Pomyślnie zresetowano hasło! Możesz się zalogować do systemu z nowymi danymi dostępu!");
        setTimeout(() =>{
          this.router.navigateByUrl('/login');
        },3000);
      },error => {
        this.errorMessage = error.error;
        this.toastr.error(this.errorMessage,'Błąd!');
      });

  }
}

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const invalidCtrl = !!(control?.invalid && control?.parent?.dirty);
    const invalidParent = !!(control?.parent?.invalid && control?.parent?.dirty);

    return invalidCtrl || invalidParent;
  }
}
