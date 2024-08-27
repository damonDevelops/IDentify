import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-redirect-id',
  templateUrl: './scan-id-redirect.component.html',
})
export class AppScanIdRedirectComponent {
  imageSrc = './assets/images/svgs/id-card.svg';
  constructor(private router: Router) {}

  redirect() {
    this.router.navigate(['/scan-id/app-common-scan-id']);
  }
}
