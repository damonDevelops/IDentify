import { Component, OnInit } from '@angular/core';
@Component({
  selector: 'app-common-scan-id',
  templateUrl: './common-scan-id.component.html',
})
export class CommonScanIdComponent implements OnInit {
  isMobile: boolean = false;

  constructor() {}

  ngOnInit() {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isMobile = window.innerWidth <= 768; // Adjust the breakpoint as needed
  }
}
