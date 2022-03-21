import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-messenger-window',
  templateUrl: './messenger-window.component.html',
  styleUrls: ['./messenger-window.component.css']
})
export class MessengerWindowComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<MessengerWindowComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    console.log(this.data);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
