import {Component, Inject, OnInit} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {TokenStorageService} from "../../services/token-storage.service";
import {AuthService} from "../../services/auth.service";
import {DOCUMENT} from "@angular/common";
import {OrganisationFormComponent} from "../organisation-form/organisation-form.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {
    username: null,
    password: null
  };
  organisationForm: any = {
    name:null,
    description:null
  };
  closeResult = '';
  isLoggedIn = false;
  login ='';
  orgCreation = false
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor(@Inject(DOCUMENT) private document: Document,
              private authService: AuthService,
              private tokenStorage: TokenStorageService,
              private toastr:ToastrService,
              public dialog: MatDialog) { }

  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
  }

  onSubmit(): void {
    const { username, password } = this.form;

    this.authService.logUser(username, password).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.login = data.username;
        if(data.organisation)
        {
          this.toastr.success('Zalogowano!')
          setTimeout(() =>{
            this.document.location.href = '/home';
          },2000);
        }
        else {
          const modalRef = this.dialog.open(OrganisationFormComponent, {
            disableClose: true,
            data:{login:this.login},
          });
          modalRef.afterClosed().subscribe(res=>{
            setTimeout(() =>{
              this.document.location.href = '/home';
            },2000);
          });
        }
      },
      err => {
        this.errorMessage = err.error;
        this.isLoginFailed = true;
        this.toastr.error(this.errorMessage,'Błąd!');
      }
    );
  }

  reloadPage(): void {
    window.location.reload();
  }
}
