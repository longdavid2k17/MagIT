import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {TokenStorageService} from "../../services/token-storage.service";
import {ToastrService} from "ngx-toastr";
import {BookmarkService} from "../../services/bookmark.service";
import {ErrorMessageClass} from "../projects/projects/projects.component";
import {
  ConfirmationDialogComponent,
  ConfirmDialogModel
} from "../general/confirmation-dialog/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {EditExampleBookmarkComponent} from "../edit-example-bookmark/edit-example-bookmark.component";
import {PreviewBookmarkComponent} from "../preview-bookmark/preview-bookmark.component";

@Component({
  selector: 'app-solutions-library',
  templateUrl: './solutions-library.component.html',
  styleUrls: ['./solutions-library.component.css']
})
export class SolutionsLibraryComponent implements OnInit,AfterViewInit {
  displayedColumns: string[] = ['action','task_title', 'type','description','creationDate'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatPaginator) paginator: MatPaginator;
  isLoading:boolean=true;
  totalElements:number=0;
  filterVal:any="";

  user:any;
  canEdit:boolean=false;
  roles:any[]=[];

  constructor(private tokenStorage: TokenStorageService,
              private toastr: ToastrService,
              public dialog: MatDialog,
              private bookmarkService:BookmarkService) { }

  ngOnInit(): void {
    this.user = this.tokenStorage.getUser();
    this.roles = this.user.roles;
    if(this.roles.includes("ROLE_PM") || this.roles.includes("ROLE_ADMIN")) this.canEdit=true;
    if(this.user.organisation.id){
      const usersRequest = {};
      // @ts-ignore
      usersRequest['page'] = 0;
      // @ts-ignore
      usersRequest['size'] = 10;
      this.getData(usersRequest);
    }
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

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  getData(request:any){
    this.isLoading=true;
    this.bookmarkService.getByOrganisation(this.user.organisation.id,request).subscribe(res => {
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

  openPreview(row: any) {
    const dialogRef = this.dialog.open(PreviewBookmarkComponent, {
      maxWidth: "400px",
      data: row,
      hasBackdrop: true,
      disableClose:true
    });
  }

  removeBookmark(row:any) {
    const message = `Czy jesteś pewny że chcesz odznaczyć usunąć to rozwiązanie?`;
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
        this.bookmarkService.delete(row.task.id).subscribe(()=>{
          this.toastr.success("Usunięto rozwiązanie!")
          this.refresh();
        },error => {
          this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!");
        });
      }
    });
  }

  refresh(): void {
    window.location.reload();
  }

  edit(row:any) {
    const modalRef = this.dialog.open(EditExampleBookmarkComponent, {
      disableClose: true,
      data:row,
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
