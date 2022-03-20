import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {HomeComponent} from "./components/home/home.component";
import {StartComponent} from "./components/start/start.component";
import {TokenComponent} from "./components/token/token.component";
import {ResetPasswordComponent} from "./components/reset-password/reset-password.component";
import {
  ResetPasswordConfirmationComponent
} from "./components/reset-password-confirmation/reset-password-confirmation.component";
import {OrganisationManagementComponent} from "./components/organisation-management/organisation-management.component";
import {ProfileManagementComponent} from "./components/profile-management/profile-management.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent },
  { path: 'start', component: StartComponent },
  { path: 'register-confirm', component: TokenComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'reset-confirm', component: ResetPasswordConfirmationComponent },
  { path: 'organisation-management', component: OrganisationManagementComponent },
  { path: 'profile', component: ProfileManagementComponent },
  { path: '', redirectTo: 'start', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
