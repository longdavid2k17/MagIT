import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {AuthService} from "../../../services/auth.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TokenStorageService} from "../../../services/token-storage.service";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {ProjectsService} from "../../../services/projects.service";

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.css']
})
export class ProjectFormComponent implements OnInit {

  form: FormGroup;
  pmUsers:any[] =[];
  isChecked:boolean=false;
  organisation:any;

  constructor(public dialogRef: MatDialogRef<ProjectFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastr:ToastrService,
              private authService:AuthService,
              private projectService:ProjectsService,
              private fb: FormBuilder,
              private tokenStorage: TokenStorageService) {
    this.form = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null],
      driveName: [null],
      projectManager: [null],
      organisation:[null],
      startDate: [null],
      endDate: [null],
      id: [null],
    });
  }

  ngOnInit(): void {
    this.organisation = this.tokenStorage.getUser().organisation;
    if(this.organisation?.id){
      this.projectService.getAllPMsFromOrg(this.organisation.id).subscribe(res=>{
        this.pmUsers = res;
      });
    }
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }
    let formVal = this.form.value;
    if(this.organisation)
      formVal.organisation = this.organisation;

    this.projectService.save(formVal).subscribe(res=>{
      this.toastr.success("Zapisano!")
    },error => {
      this.toastr.error(error,"Błąd");
    });
  }

  onDriveChange(event: MatSlideToggleChange) {
    this.isChecked = event.checked;
  }
}
