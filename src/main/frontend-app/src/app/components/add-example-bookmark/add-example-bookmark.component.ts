import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ConfirmDialogModel} from "../general/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-add-example-bookmark',
  templateUrl: './add-example-bookmark.component.html',
  styleUrls: ['./add-example-bookmark.component.css']
})
export class AddExampleBookmarkComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<AddExampleBookmarkComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogModel) { }

  ngOnInit(): void {
  }

  dismiss() {
    this.dialogRef.close();
  }

  decline() {

  }

  save() {

  }
}
