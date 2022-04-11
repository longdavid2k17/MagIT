import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {MessengerWindowComponent} from "../../messenger-window/messenger-window.component";
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {ProjectFormComponent} from "../project-form/project-form.component";
import {ProjectsService} from "../../../services/projects.service";
import {TokenStorageService} from "../../../services/token-storage.service";

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

      this.projectsService.getAllProjectsForOrg(this.user.organisation.id).subscribe(res => {
        this.dataSource = new MatTableDataSource(res);
          this.dataSource.paginator = this.paginator;
      },
        error => {
        this.toastr.error(error,"Błąd podczas pobierania danych!");
        });
    }
  }


  editProject(row : PeriodicElement) {

  }

  showTasks(row : PeriodicElement) {

  }

  addProject() {
    this.dialog.open(ProjectFormComponent, {
      autoFocus: false,
      disableClose: true,
      hasBackdrop: true
    });
  }

  applyFilter(x: any) {
    const searchString = x.target.value;
    console.log(searchString);
  }
}
export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}
