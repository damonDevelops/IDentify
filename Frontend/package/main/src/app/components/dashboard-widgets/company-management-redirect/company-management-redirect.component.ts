import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-company-management',
  templateUrl: './company-management-redirect.component.html',
})
export class AppCompanyManagementRedirectComponent {
  imageSrc = './assets/images/svgs/id-card.svg';
  constructor(private router: Router) {}

  redirect() {
    this.router.navigate(['/theme-pages/company-management']);
  }
}
