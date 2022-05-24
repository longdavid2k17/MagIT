import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {TokenStorageService} from "../../services/token-storage.service";
import {ErrorMessageClass} from "../projects/projects/projects.component";
import {UserService} from "../../services/user.service";
import {
  ConfirmationDialogComponent,
  ConfirmDialogModel
} from "../general/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-user-managment',
  templateUrl: './user-managment.component.html',
  styleUrls: ['./user-managment.component.css']
})
export class UserManagmentComponent implements OnInit,AfterViewInit {

  displayedColumns: string[] = ['name', 'surname','email','username', 'enabled','lastLogged','action'];
  dataSource = new MatTableDataSource();
  totalUsersElements: number = 0;
  user:any;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  searchQueryVal:any;

  users:any[] = [];

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  constructor(private toastr:ToastrService,
              public dialog: MatDialog,
              private userService:UserService,
              private tokenStorage: TokenStorageService) { }

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

  getData(request?:any){
    this.userService.getByOrganisationId(this.user.organisation.id,request).subscribe(res => {
        // @ts-ignore
        this.users = res['content'];
        // @ts-ignore
        this.totalUsersElements = res['totalElements'];
        // @ts-ignore
        this.dataSource = new MatTableDataSource(res['content']);
        this.dataSource.paginator = this.paginator;
      },
      error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
  }

  nextUserPage(event: PageEvent) {
    const request = {};
    if(this.searchQueryVal){
      // @ts-ignore
      usersRequest['search'] = this.searchQueryVal;
    }
    // @ts-ignore
    request['page'] = event.pageIndex.toString();
    // @ts-ignore
    request['size'] = event.pageSize.toString();
    this.getData(request);
  }

  applyFilter(x: any) {
    const searchString = x.target.value;
    if(searchString && searchString.length){
      this.searchQueryVal = searchString;
      const usersRequest = {};
      // @ts-ignore
      usersRequest['page'] = 0;
      // @ts-ignore
      usersRequest['size'] = 10;
      // @ts-ignore
      usersRequest['search'] = searchString;

      this.getData(usersRequest);
    }
    else {
      this.searchQueryVal = '';
      const usersRequest = {};
      // @ts-ignore
      usersRequest['page'] = 0;
      // @ts-ignore
      usersRequest['size'] = 10;

      this.getData(usersRequest);
    }
  }

  activateUser(row:any) {
    const message = `Czy jesteś pewny że chcesz aktywować tego użytkownika?`;
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
        this.userService.enableAccount(row).subscribe(res=>{
          this.toastr.success("Aktywowano użytkownika!")
          this.refresh();
        },error => {
          this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!");
        });
      }
    });
  }

  deactivateUser(row:any) {
    const message = `Czy jesteś pewny że chcesz dezaktywować tego użytkownika?`;
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
        this.userService.disableAccount(row).subscribe(res=>{
          this.toastr.success("Dezaktywowano użytkownika!")
          this.refresh();
        },error => {
          this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd!");
        });
      }
    });
  }

  deleteUser(row:any) {
    const message = `Czy jesteś pewny że chcesz usunąć tego użytkownika?`;
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
        this.userService.delete(row).subscribe(res=>{
          this.toastr.success("Usunięto użytkownika!")
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
}
