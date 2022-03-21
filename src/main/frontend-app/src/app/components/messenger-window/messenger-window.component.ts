import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MessengerService} from "../../services/messenger.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-messenger-window',
  templateUrl: './messenger-window.component.html',
  styleUrls: ['./messenger-window.component.css']
})
export class MessengerWindowComponent implements OnInit {
  form: any = {
    message: null
  };
  constructor(public dialogRef: MatDialogRef<MessengerWindowComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private messengerService:MessengerService,
              private toastr:ToastrService) { }

  ngOnInit(): void {
    console.log(this.data);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  sendMessage() {
    const { message } = this.form;

    if(message!=null || message!="")
    {
      this.messengerService.send(this.data.userId,this.data.messengerInstance.interlocutor.id,message)
        .subscribe(res=>{
      },error => {
          this.toastr.error(error.errorMessage,"Błąd podczas wysyłania!");
        })
    }
  }
}
