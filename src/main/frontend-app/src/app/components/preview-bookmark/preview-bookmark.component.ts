import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ErrorMessageClass} from "../projects/projects/projects.component";
import {ResultFileUploadService} from "../../services/result-file-upload.service";
import {AttachmentFileUploadService} from "../../services/attachment-file-upload.service";

@Component({
  selector: 'app-preview-bookmark',
  templateUrl: './preview-bookmark.component.html',
  styleUrls: ['./preview-bookmark.component.css']
})
export class PreviewBookmarkComponent implements OnInit {

  entryData:any;
  resultFileInfos?: any[]=[];
  attachmentFileInfos?: any[]=[];
  resourcesMessage:any;

  constructor(public dialogRef: MatDialogRef<PreviewBookmarkComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private uploadService:ResultFileUploadService,
              private attachmentService:AttachmentFileUploadService) {
    this.entryData = data;
    this.uploadService.getFiles(this.entryData?.task.id).subscribe(res=>{
      this.resultFileInfos = res;
    },error => {
      this.resourcesMessage = ErrorMessageClass.getErrorMessage(error);
    });
    this.attachmentService.getFiles(this.entryData?.task.id).subscribe(res=>{
      this.attachmentFileInfos = res;
    },error => {
      this.resourcesMessage = ErrorMessageClass.getErrorMessage(error);
    });
  }

  ngOnInit(): void {
  }

  dismiss() {
    this.dialogRef.close();
  }
}
