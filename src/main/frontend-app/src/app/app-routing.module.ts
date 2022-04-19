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
import {TeamDashboardComponent} from "./components/teams/team-dashboard/team-dashboard.component";
import {RoleGuard} from "./core/role.guard";
import {PerformanceComponent} from "./components/performance/performance.component";
import {SolutionsLibraryComponent} from "./components/solutions-library/solutions-library.component";
import {ProjectsComponent} from "./components/projects/projects/projects.component";
import {TasksRegisterComponent} from "./components/tasks/tasks-register/tasks-register.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent },
  { path: 'start', component: StartComponent },
  { path: 'register-confirm', component: TokenComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'reset-confirm', component: ResetPasswordConfirmationComponent },
  { path: 'solutions', component: SolutionsLibraryComponent },
  {
    path: 'projects',
    component: ProjectsComponent,
    canActivate:[RoleGuard],
    data:{
      expectedRoles:['ROLE_ADMIN']
    }
  },
  {
    path: 'performance',
    component: PerformanceComponent,
    canActivate:[RoleGuard],
    data:{
      expectedRoles:['ROLE_ADMIN']
    }
  },
  {
    path: 'teams',
    component: TeamDashboardComponent,
    canActivate:[RoleGuard],
    data:{
      expectedRoles:['ROLE_ADMIN']
    }
  },
  {
    path: 'tasks',
    component: TasksRegisterComponent,
    canActivate:[RoleGuard],
    data:{
      expectedRoles:['ROLE_ADMIN','ROLE_PM']
    }
  },
  { path: '', redirectTo: 'start', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
