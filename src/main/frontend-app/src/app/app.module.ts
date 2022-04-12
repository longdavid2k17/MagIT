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
import {MatTabsModule} from "@angular/material/tabs";
import {DatePipe} from "@angular/common";
import { MessengerWindowComponent } from './components/messenger-window/messenger-window.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule, MatOptionModule} from "@angular/material/core";
import {ClipboardModule} from "@angular/cdk/clipboard";
import { TeamDashboardComponent } from './components/teams/team-dashboard/team-dashboard.component';
import {MatCardModule} from "@angular/material/card";
import { RoleFormComponent } from './components/teams/role-form/role-form.component';
import {MatSelectModule} from "@angular/material/select";
import { OrganisationFormComponent } from './components/organisation-form/organisation-form.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import { PerformanceComponent } from './components/performance/performance.component';
import { SolutionsLibraryComponent } from './components/solutions-library/solutions-library.component';
import { ProjectsComponent } from './components/projects/projects/projects.component';
import { OrganisationRoleChooserComponent } from './components/organisation-role-chooser/organisation-role-chooser.component';
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatMenuModule} from "@angular/material/menu";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatPaginatorModule} from "@angular/material/paginator";
import { TeamFormComponent } from './components/teams/team-form/team-form.component';
import {MatTableModule} from "@angular/material/table";
import { ProjectFormComponent } from './components/projects/project-form/project-form.component';
import { ConfirmationDialogComponent } from './components/general/confirmation-dialog/confirmation-dialog.component';
import {MatStepperModule} from "@angular/material/stepper";

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
    ProfileManagementComponent,
    MessengerWindowComponent,
    TeamDashboardComponent,
    RoleFormComponent,
    OrganisationFormComponent,
    PerformanceComponent,
    SolutionsLibraryComponent,
    ProjectsComponent,
    OrganisationRoleChooserComponent,
    TeamFormComponent,
    ProjectFormComponent,
    ConfirmationDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgMultiSelectDropDownModule.forRoot(),
    MatTabsModule,
    MatDialogModule,
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
    BrowserAnimationsModule,
    DragDropModule,
    MatFormFieldModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ClipboardModule,
    MatCardModule,
    MatSelectModule,
    MatOptionModule,
    MatProgressSpinnerModule,
    MatSlideToggleModule,
    MatMenuModule,
    MatCheckboxModule,
    MatPaginatorModule,
    MatTableModule,
    MatStepperModule
  ],
  providers: [
    MatDatepickerModule,
    { provide: HTTP_INTERCEPTORS, useClass: UserEmulationInterceptor, multi: true },
    DatePipe,
    {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
