import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MessengerService} from "../../services/messenger.service";
import {ToastrService} from "ngx-toastr";
import * as SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

@Component({
  selector: 'app-messenger-window',
  templateUrl: './messenger-window.component.html',
  styleUrls: ['./messenger-window.component.css']
})
export class MessengerWindowComponent implements OnInit {
  disabled = true;
  newmessage: string;
  greetings: IMessage[] = [];
  interluctorId:number;
  userId:number;

  private stompClient = null;

  form: any = {
    message: null
  };
  constructor(public dialogRef: MatDialogRef<MessengerWindowComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private messengerService:MessengerService,
              private toastr:ToastrService) {
    this.greetings = data.messengerInstance.messages;
    this.interluctorId = data.messengerInstance.interlocutor.id;
    this.userId = data.userId;
  }

  ngOnInit(): void {
    this.connect();
  }

  connect() {
    const socket = new SockJS('http://localhost:8080/messenger');
    // @ts-ignore
    this.stompClient = Stomp.over(socket);

    const _this = this;
    // @ts-ignore
    this.stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);

      // @ts-ignore
      _this.stompClient.subscribe('/start/initial', function (hello) {
/*        console.log(JSON.parse(hello.body));*/
        _this.showMessage(JSON.parse(hello.body));
      });
    });
  }

  setConnected(connected: boolean) {
    this.disabled = !connected;

    if (connected) {
      this.greetings = [];
    }
  }

  sendMessageWs(message:any) {
    // @ts-ignore
    this.stompClient.send(
      '/current/resume',
      {},
      JSON.stringify(message)
    );
    var a:any=
      {
        authorUserId:this.userId,
        text:message
      };
    this.greetings.push(a);
  }

  showMessage(message:any) {
    var incoming:any=
      {
        authorUserId:this.interluctorId,
        text:message
    };
    this.greetings.push(incoming);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  sendMessage() {
    const { message } = this.form;

    if(message!=null && message!="" && message.length>0)
    {
      this.sendMessageWs(message);
      this.messengerService.send(this.data.userId,this.data.messengerInstance.interlocutor.id,message)
        .subscribe(res=>{
      },error => {
          this.toastr.error(error.errorMessage,"Błąd podczas wysyłania!");
        })
    }
  }
}

export interface IMessage{
  id:number;
  authorUserId:number;
  targetUserId:number;
  text:string;
  sendDate:Date;
  isRead:boolean;
}
