import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-tasks-register',
  templateUrl: './tasks-register.component.html',
  styleUrls: ['./tasks-register.component.css']
})
export class TasksRegisterComponent implements OnInit {
  displayedColumns: string[] = ['action','name', 'project','endDate'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatPaginator) paginator: MatPaginator;
  isLoading:boolean=true;
  totalElements:number=0;

  constructor() { }

  ngOnInit(): void {
  }

  nextPage(event: PageEvent) {

  }

  applyFilter(x: any) {
    const searchString = x.target.value;
  }

  addTask() {

  }
}
