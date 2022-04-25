import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {TokenStorageService} from "../../../services/token-storage.service";
import {ErrorMessageClass} from "../../projects/projects/projects.component";
import {TaskService} from "../../../services/task.service";
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {CreateTaskFormComponent} from "../create-task-form/create-task-form.component";
import {
  ConfirmationDialogComponent,
  ConfirmDialogModel
} from "../../general/confirmation-dialog/confirmation-dialog.component";
import {TeamEditFormComponent} from "../../teams/team-edit-form/team-edit-form.component";
import {TaskPreviewComponent} from "../task-preview/task-preview.component";

@Component({
  selector: 'app-tasks-register',
  templateUrl: './tasks-register.component.html',
  styleUrls: ['./tasks-register.component.css']
})
export class TasksRegisterComponent implements OnInit,AfterViewInit {
  displayedColumns: string[] = ['action','name', 'project','status','endDate','complete'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatPaginator) paginator: MatPaginator;
  isLoading:boolean=true;
  totalElements:number=0;

  user:any;

  constructor(private tokenStorage: TokenStorageService,
              private tasksService: TaskService,
              private toastr: ToastrService,
              public dialog: MatDialog,) { }

  ngOnInit(): void {
    this.user = this.tokenStorage.getUser();
    if(this.user.organisation.id){
      const usersRequest = {};
      // @ts-ignore
      usersRequest['page'] = 0;
      // @ts-ignore
      usersRequest['size'] = 10;
      this.getData(usersRequest);
    }
  }

  getData(request:any){
    this.isLoading=true;
    this.tasksService.getByOrganisationIdPageable(this.user.organisation.id,request).subscribe(res => {
        // @ts-ignore
        this.teams = res['content'];
        // @ts-ignore
        this.totalTeamsElements = res['totalElements'];
        // @ts-ignore
        this.dataSource = new MatTableDataSource(res['content']);
        this.dataSource.paginator = this.paginator;
      },
      error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
    this.isLoading=false;
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  nextPage(event: PageEvent) {

  }

  applyFilter(x: any) {
    const searchString = x.target.value;
  }

  addTask() {
    this.dialog.open(CreateTaskFormComponent, {
      data:{organisation:this.user.organisation},
      autoFocus: false,
      disableClose: true,
      hasBackdrop: true
    });
  }

  delete(row:any) {
    const message = `Czy jesteś pewny że chcesz usunąć te zadanie?`;
    const dialogData = new ConfirmDialogModel("Wymagane potwierdzenie", message);
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      maxWidth: "400px",
      data: dialogData,
      hasBackdrop: true,
      disableClose:true
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if(dialogResult)
      {
        this.tasksService.delete(row.id).subscribe(res=>{
          this.toastr.success("Usunięto zadanie!")
        },error => {
          this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!");
        });
      }
    });
  }

  openPreview(row:any) {
    const modalRef = this.dialog.open(TaskPreviewComponent, {
      disableClose: true,
      data:{task:row},
      hasBackdrop: true,
      panelClass: 'my-dialog',
      minWidth:"600px"
    });
    modalRef.afterClosed().subscribe(res =>{
      this.refresh();
    });
  }

  refresh(): void {
    window.location.reload();
  }
}
