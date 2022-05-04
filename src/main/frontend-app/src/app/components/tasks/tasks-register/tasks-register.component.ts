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
import {TaskPreviewComponent} from "../task-preview/task-preview.component";
import {AddExampleBookmarkComponent} from "../../add-example-bookmark/add-example-bookmark.component";
import {BookmarkService} from "../../../services/bookmark.service";
import {EditTaskFormComponent} from "../edit-task-form/edit-task-form.component";

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
  filterVal:any="";
  user:any;

  constructor(private tokenStorage: TokenStorageService,
              private tasksService: TaskService,
              private toastr: ToastrService,
              public dialog: MatDialog,
              private bookmarkService:BookmarkService) { }

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
        this.totalElements = res['totalElements'];
        // @ts-ignore
        this.dataSource = new MatTableDataSource(res['content']);
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
    const usersRequest = {};
    // @ts-ignore
    usersRequest['page'] = event.pageIndex+1;
    // @ts-ignore
    usersRequest['size'] = event.pageSize;
    if(this.filterVal.length>0) {
      // @ts-ignore
      usersRequest['query'] = searchString;
    }
    this.getData(usersRequest);
  }

  applyFilter(x: any) {
    const searchString = x.target.value;
    if(searchString.length>0){
      this.filterVal=searchString;
      const usersRequest = {};
      // @ts-ignore
      usersRequest['page'] = 0;
      // @ts-ignore
      usersRequest['size'] = 10;
      // @ts-ignore
      usersRequest['query'] = searchString;
      this.getData(usersRequest);
    }
    else {
      this.filterVal="";
      const usersRequest = {};
      // @ts-ignore
      usersRequest['page'] = 0;
      // @ts-ignore
      usersRequest['size'] = 10;
      this.getData(usersRequest);
    }
  }

  addTask() {
    let modalRef = this.dialog.open(CreateTaskFormComponent, {
      data:{organisation:this.user.organisation},
      autoFocus: false,
      disableClose: true,
      hasBackdrop: true
    });
    modalRef.afterClosed().subscribe(res =>{
      this.refresh();
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

  setBookmark(row: any) {
    const modalRef = this.dialog.open(AddExampleBookmarkComponent, {
      disableClose: true,
      data:{task:row},
      hasBackdrop: true,
      panelClass: 'my-dialog',
      minWidth:"600px"
    });
    modalRef.afterClosed().subscribe(res =>{
      if(res?.id){
        this.refresh();
      }
    });
  }

  removeBookmark(row: any) {
    const message = `Czy jesteś pewny że chcesz odznaczyć usunąć to zadanie z rejestru przykładowych rozwiązań?`;
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
        this.bookmarkService.delete(row.id).subscribe(()=>{
          this.toastr.success("Usunięto rozwiązanie!")
          this.refresh();
        },error => {
          this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!");
        });
      }
    });
  }

  edit(row:any) {
    const modalRef = this.dialog.open(EditTaskFormComponent, {
      disableClose: true,
      data:{
        task:row,
        organisation:this.user.organisation
      },
      hasBackdrop: true,
      panelClass: 'my-dialog',
      minWidth:"600px"
    });
    modalRef.afterClosed().subscribe(res =>{
      if(res?.id){
        this.refresh();
      }
    });
  }
}
