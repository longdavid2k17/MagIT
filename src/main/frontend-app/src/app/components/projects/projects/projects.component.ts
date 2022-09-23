import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {ProjectFormComponent} from "../project-form/project-form.component";
import {ProjectsService} from "../../../services/projects.service";
import {TokenStorageService} from "../../../services/token-storage.service";
import {
  ConfirmationDialogComponent,
  ConfirmDialogModel
} from "../../general/confirmation-dialog/confirmation-dialog.component";
import {ProjectTasksRegistryComponent} from "../../tasks/project-tasks-registry/project-tasks-registry.component";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit,AfterViewInit  {

  displayedColumns: string[] = ['name', 'description','startDate','endDate', 'tasks','today_tasks','action'];
  dataSource = new MatTableDataSource();
  user:any;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  constructor(private toastr:ToastrService,
              public dialog: MatDialog,
              private projectsService:ProjectsService,
              private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
    this.user = this.tokenStorage.getUser();
    if(this.user.organisation.id){
      this.getData();
    }
  }

  getData(search?:any){
    this.projectsService.getAllProjectsForOrg(this.user.organisation.id,search).subscribe(res => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.paginator = this.paginator;
      },
      error => {
        this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd podczas pobierania danych!");
      });
  }


  editProject(row : any) {
    this.dialog.open(ProjectFormComponent, {
      autoFocus: false,
      disableClose: true,
      data:{
        existingProj:row
      },
      hasBackdrop: true
    });
  }

  showTasks(row : any) {
    const dialogRef = this.dialog.open(ProjectTasksRegistryComponent, {
      data:row.id,
      autoFocus: false,
      disableClose: true,
      hasBackdrop: true
    });
    dialogRef.afterClosed().subscribe();
  }

  addProject() {
    const dialogRef = this.dialog.open(ProjectFormComponent, {
      autoFocus: false,
      disableClose: true,
      hasBackdrop: true
    });
    dialogRef.afterClosed().subscribe(res =>{
      this.refresh();
    });
  }

  applyFilter(x: any) {
    const searchString = x.target.value;
    if(searchString && searchString.length)
      this.getData(searchString);
    else this.getData();
  }

  deleteProject(row: any) {
    const message = `Czy jesteś pewny że chcesz usunąć ten projekt?`;
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
        this.projectsService.deleteProject(row.id).subscribe(res=>{
          this.toastr.success("Usunięto projekt!")
        },error => {
          const errorMessage = ErrorMessageClass.getErrorMessage(error);
          this.toastr.error(errorMessage,"Błąd!");
        });
      }
    });
  }

  archiveProject(row: any) {
    const message = `Czy jesteś pewny że chcesz zarchiwizować ten projekt?`;
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
        this.projectsService.archiveProject(row.id).subscribe(res=>{
          this.toastr.success("Zarchiwizowano projekt!")
        },error => {
          const errorMessage = ErrorMessageClass.getErrorMessage(error);
          this.toastr.error(errorMessage,"Błąd!");
        });
      }
    });
  }

  refresh(): void {
    window.location.reload();
  }
}

export class ErrorMessageClass {
  public static getErrorMessage(err: any):any {
    const detail =
      this.getNotEmpty(err?.error?.message) ??
      this.getNotEmpty(err?.error?.detail) ??
      this.getNotEmpty(err?.error?.error) ??
      this.getNotEmpty(err?.error) ??
      this.getNotEmpty(err?.message) ??
      this.getNotEmpty(err?.statusText) ??
      this.getNotEmpty(err) ??
      undefined;
    return detail;
  }

  private static getNotEmpty(val?: unknown): string | undefined {
    if (val && typeof val === 'string' && val.length) {
      return val;
    }
    return;
  }
}
