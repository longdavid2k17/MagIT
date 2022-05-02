import {Component, Input, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {ResultFileUploadService} from "../../../services/result-file-upload.service";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {ErrorMessageClass} from "../../projects/projects/projects.component";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-result-file-upload',
  templateUrl: './result-file-upload.component.html',
  styleUrls: ['./result-file-upload.component.css']
})
export class ResultFileUploadComponent implements OnInit {

  @Input()
  taskId:any;

  resourcesMessage:any;

  currentFile?: File;
  progress = 0;
  message = '';
  fileName = 'Wybierz plik';
  fileInfos?: any[]=[];
  constructor(private uploadService: ResultFileUploadService,
              private toastrService:ToastrService) { }

  ngOnInit(): void {
    this.getFiles();
  }

  getFiles():void{
    this.uploadService.getFiles(this.taskId).subscribe(res=>{
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
      this.uploadService.upload(this.currentFile,this.taskId).subscribe(
        (event: any) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progress = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            this.toastrService.success("Pomyślnie przesłano plik do tego zadania!")
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
