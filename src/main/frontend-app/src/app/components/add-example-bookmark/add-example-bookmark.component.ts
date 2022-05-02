import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {BookmarkService} from "../../services/bookmark.service";
import {ToastrService} from "ngx-toastr";
import {ErrorMessageClass} from "../projects/projects/projects.component";
import {Observable} from "rxjs";

@Component({
  selector: 'app-add-example-bookmark',
  templateUrl: './add-example-bookmark.component.html',
  styleUrls: ['./add-example-bookmark.component.css']
})
export class AddExampleBookmarkComponent implements OnInit {
  form:FormGroup;
  task:any;
  allTypes:any[]=[];
  myControl = new FormControl();

  constructor(public dialogRef: MatDialogRef<AddExampleBookmarkComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private fb:FormBuilder,
              private bookmarkService:BookmarkService,
              private toastr:ToastrService) {
    this.task = data.task;
    console.log(this.task);
    this.form = this.fb.group({
      id: [null],
      organisation: [null],
      task: [null],
      type:[null],
      description:[null],
      creationDate:[null]
    });
  }

  dismiss() {
    this.dialogRef.close();
  }

  save() {
    let form = this.form.value;
    form.task = this.task;
    form.organisation = this.task?.organisation;
    this.bookmarkService.save(form).subscribe(res=>{
      this.dialogRef.close(res);
      },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }

  ngOnInit(): void {
    this.bookmarkService.getAllTypes(this.task?.organisation.id).subscribe(res=>{
      this.allTypes=res;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }

}
