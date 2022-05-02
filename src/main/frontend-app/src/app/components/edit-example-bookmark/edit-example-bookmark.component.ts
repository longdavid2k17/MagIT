import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BookmarkService} from "../../services/bookmark.service";
import {ToastrService} from "ngx-toastr";
import {ErrorMessageClass} from "../projects/projects/projects.component";

@Component({
  selector: 'app-edit-example-bookmark',
  templateUrl: './edit-example-bookmark.component.html',
  styleUrls: ['./edit-example-bookmark.component.css']
})
export class EditExampleBookmarkComponent implements OnInit {
  form:FormGroup;
  allTypes:any[]=[];
  myControl = new FormControl();

  bookmark:any;

  constructor(public dialogRef: MatDialogRef<EditExampleBookmarkComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private fb:FormBuilder,
              private bookmarkService:BookmarkService,
              private toastr:ToastrService) {
    this.bookmark = data;
    this.form = this.fb.group({
      id: [null],
      organisation: [null],
      task: [null],
      type:[null],
      description:[null],
      creationDate:[null]
    });
    this.form.patchValue(this.bookmark);
  }

  dismiss() {
    this.dialogRef.close();
  }

  save() {
    let form = this.form.value;
    this.bookmarkService.save(form).subscribe(res=>{
      this.dialogRef.close(res);
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }

  ngOnInit(): void {
    this.bookmarkService.getAllTypes(this.bookmark?.task?.organisation.id).subscribe(res=>{
      this.allTypes=res;
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }
}
