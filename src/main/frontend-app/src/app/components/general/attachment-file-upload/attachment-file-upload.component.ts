import {Component, Input, OnInit} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {AttachmentFileUploadService} from "../../../services/attachment-file-upload.service";
import {ErrorMessageClass} from "../../projects/projects/projects.component";
import {HttpEventType, HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-attachment-file-upload',
  templateUrl: './attachment-file-upload.component.html',
  styleUrls: ['./attachment-file-upload.component.css']
})
export class AttachmentFileUploadComponent implements OnInit {

  @Input()
  taskId:any;

  resourcesMessage:any;

  currentFile?: File;
  progress = 0;
  message = '';
  fileName = 'Wybierz plik';
  fileInfos?: any[]=[];

  constructor(private toastrService:ToastrService,
              private fileUploadService:AttachmentFileUploadService) { }

  ngOnInit(): void {
    this.getFiles();
  }

  getFiles():void{
    this.fileUploadService.getFiles(this.taskId).subscribe(res=>{
      this.fileInfos = res;
    },error => {
      this.resourcesMessage = ErrorMessageClass.getErrorMessage(error);
    });
  }

  selectFile(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file: File = event.target.files[0];
      this.currentFile = file;
      this.fileName = this.currentFile.name;
    } else {
      this.fileName = 'Wybierz plik';
    }
  }
  upload(): void {
    this.progress = 0;
    this.message = "";
    if (this.currentFile) {
      this.fileUploadService.upload(this.currentFile,this.taskId).subscribe(
        (event: any) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progress = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            this.toastrService.success("Pomyślnie przesłano załącznik!")
            this.getFiles();
          }
        },
        (err) => {
          this.progress = 0;
          this.message = ErrorMessageClass.getErrorMessage(err);
          this.currentFile = undefined;
        });
    }
  }

}
