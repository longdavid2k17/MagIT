import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { StartComponent } from './components/start/start.component';
import {ToastrModule} from "ngx-toastr";
import {NgMultiSelectDropDownModule} from "ng-multiselect-dropdown";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { TokenComponent } from './components/token/token.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { ResetPasswordConfirmationComponent } from './components/reset-password-confirmation/reset-password-confirmation.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { OrganisationManagementComponent } from './components/organisation-management/organisation-management.component';
import { ProfileManagementComponent } from './components/profile-management/profile-management.component';
import {UserEmulationInterceptor} from "./interceptors/user-emulation.interceptor";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    StartComponent,
    TokenComponent,
    ResetPasswordComponent,
    ResetPasswordConfirmationComponent,
    OrganisationManagementComponent,
    ProfileManagementComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgMultiSelectDropDownModule.forRoot(),
    ToastrModule.forRoot({
        timeOut: 5000,
        progressBar: true,
        progressAnimation: 'increasing',
        preventDuplicates: true,
        positionClass: 'toast-top-right'
      }
    ),
    NgbModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: UserEmulationInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
