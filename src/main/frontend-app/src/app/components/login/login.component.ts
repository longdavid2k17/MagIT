import {Component, Inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {TokenStorageService} from "../../services/token-storage.service";
import {AuthService} from "../../services/auth.service";
import {DOCUMENT} from "@angular/common";
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";

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
              private router: Router,
              private modalService:NgbModal) { }

  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
  }

  onSubmit(content:any): void {
    const { username, password } = this.form;

    this.authService.logUser(username, password).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.login = data.username;
        if(data.organization)
        {
          this.toastr.success('Zalogowano!')
          setTimeout(() =>{
            this.document.location.href = '/home';
          },2000);
        }
        else {
          this.orgCreation=true;
          this.modalService.open( content,{ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
            this.closeResult = `Closed with: ${result}`;
          }, (reason) => {
            this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
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

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  reloadPage(): void {
    window.location.reload();
  }

}
