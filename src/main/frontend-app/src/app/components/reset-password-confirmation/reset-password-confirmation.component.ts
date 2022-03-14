import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {timeout} from "rxjs/operators";

@Component({
  selector: 'app-reset-password-confirmation',
  templateUrl: './reset-password-confirmation.component.html',
  styleUrls: ['./reset-password-confirmation.component.css']
})
export class ResetPasswordConfirmationComponent implements OnInit {
  token: string | null = '';
  isValid=false;
  form: any = {
    password: null,
    repassword: null
  };
  errorMessage = '';
  constructor(private authService: AuthService, private route: ActivatedRoute,private toastr:ToastrService,private router: Router) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
    if(this.token)
    this.isValid=true;


  }

  resetPassword() {
    const { password,repassword } = this.form;
    if(password==repassword)
    {
      this.authService.setNewPassword(password,this.token).subscribe(response => {
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
}
