import { Component, OnInit } from '@angular/core';
import {Chart} from 'chart.js';
import {PerformanceService} from "../../services/performance.service";
import {ToastrService} from "ngx-toastr";
import {TokenStorageService} from "../../services/token-storage.service";
import {ErrorMessageClass} from "../projects/projects/projects.component";

@Component({
  selector: 'app-performance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.css']
})
export class PerformanceComponent implements OnInit {

  constructor(private performanceService:PerformanceService,
              private toastr:ToastrService,
              private tokenService:TokenStorageService) { }

  summaryChartData:any[]=[];
  todayChartData:any[]=[];

  ngOnInit() {
    this.getData();
  }

  getData():void{
    const orgId = this.tokenService.getUser()?.organisation?.id;
    this.generateTodayChart(orgId);
    this.generateLast30DaysChart(orgId);
  }

  generateTodayChart(orgId:any):void{
    this.performanceService.getDataForTodayChart(orgId).subscribe(res=>{
      this.todayChartData=res;
      console.log(this.todayChartData)
      var barChartData = {
        labels: ["8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"],
        datasets: [{
          label: 'Ilość zadań',
          borderWidth: 1,
          data: this.todayChartData,
          color:"blue"
        }]
      };

      var ctx = document.getElementById("todayChart");
      // @ts-ignore
      var myChart = new Chart(ctx,{
        type: 'bar',
        data: barChartData,
        options: {
          responsive: true,
          legend: {
            position: 'top',
          },
        }
      });
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }

  generateLast30DaysChart(orgId:any):void{
    let summaryData:any[]=[];
    let summaryLabels:any[]=[];

    this.performanceService.getDataForSummaryChart(orgId).subscribe(res=>{
      this.summaryChartData=res;
      for(let i=0;i<this.summaryChartData.length;i++){
        summaryLabels.push(this.summaryChartData[i].projectName);
        summaryData.push(this.summaryChartData[i].taskCount);
      }
      var config = {
        type: 'doughnut',
        data: {
          datasets: [{
            data: summaryData,
            label: 'Dataset 1'
          }],
          labels: summaryLabels
        },
        options: {
          responsive: true,
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Chart.js Doughnut Chart'
          },
          animation: {
            animateScale: true,
            animateRotate: true
          }
        }
      };

      var ctx = document.getElementById("last30days");
      // @ts-ignore
      var myChart = new Chart(ctx,config);
    },error => {
      this.toastr.error(ErrorMessageClass.getErrorMessage(error),"Błąd");
    });
  }

}
