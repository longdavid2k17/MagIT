import { Component, OnInit } from '@angular/core';
import {Chart} from 'chart.js';

@Component({
  selector: 'app-performance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.css']
})
export class PerformanceComponent implements OnInit {

  constructor() { }
  ngOnInit() {
    this.generateSummaryChart();
    this.generateLast30DaysChart();
  }

  generateSummaryChart():void{
    var barChartData = {
      labels: ["8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"],
      datasets: [{
        label: 'Ilość zadań',
        borderWidth: 1,
        data: [
          0,2,1,0,2,4,1,2,4,0,0
        ],
        color:"blue"
      }]
    };

    var ctx = document.getElementById("summary");
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
  }

  generateLast30DaysChart():void{
    var config = {
      type: 'doughnut',
      data: {
        datasets: [{
          data: [
            5,10,20,40,1,2
          ],
          label: 'Dataset 1'
        }],
        labels: [
          "Red",
          "Orange",
          "Yellow",
          "Green",
          "Blue"
        ]
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
  }

}
