import {Component, Inject, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OrganisationRolesService} from "../../services/organisation-roles.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-organisation-role-chooser',
  templateUrl: './organisation-role-chooser.component.html',
  styleUrls: ['./organisation-role-chooser.component.css']
})
export class OrganisationRoleChooserComponent implements OnInit {
  form: FormGroup;
  availableRoles:any[] = [];
  user:any;

  constructor(public dialogRef: MatDialogRef<OrganisationRoleChooserComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private rolesService:OrganisationRolesService,
              private fb:FormBuilder,
              private toastr:ToastrService) {
    this.form = this.fb.group({
      organisationRoles: new FormArray([])
    });
    this.user = data.user;
    this.rolesService.getByOrganisationId(this.user.organisation.id).subscribe(res=>{
      this.availableRoles=res;
      this.addCheckboxesToForm();
      if(this.user?.organisationRoles)
        this.updateForm(this.user.organisationRoles);
    });
  }

  get orgRolesFormArray() {
    return this.form.controls.organisationRoles as FormArray;
  }

  private addCheckboxesToForm() {
    this.availableRoles.forEach(() => this.orgRolesFormArray.push(new FormControl(false)));
  }

  ngOnInit(): void {
  }

  updateForm(actualRoles:any[]){
    for(let j=0;j<actualRoles.length;j++)
    {
      for(let i=0;i<this.availableRoles.length;i++)
      {
        if(this.availableRoles[i].id==actualRoles[j].id) {
          this.orgRolesFormArray.controls[i].setValue(true);
          break;
        }
      }
    }

  }

  onSubmit() {
    let selected:any[] = [];
    for(let i=0;i<this.availableRoles.length;i++)
    {
      if(this.form.value.organisationRoles[i]===true)
        selected.push(this.availableRoles[i]);
    }
    this.rolesService.saveToUser(selected,this.user.id).subscribe(res=>{
      this.toastr.success(res.message,"Zapisano!")
      this.dialogRef.close();
    },error => {
      this.toastr.error(error.error,"Błąd podczas zapisywania!")
    })
  }
}
