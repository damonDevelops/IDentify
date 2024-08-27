import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-scan-history-component',
  templateUrl: './scan-history.component.html',
})
export class AppScanHistoryRedirectWidget {
  constructor(private router: Router) {}

  redirect() {
    this.router.navigate(['/tables/scan-history']);
  }
}
