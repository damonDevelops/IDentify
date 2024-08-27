import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-form-management',
  templateUrl: './form-management-redirect.component.html',
})
export class AppFormManagementRedirectComponent {
  imageSrc = './assets/images/svgs/id-card.svg';
  constructor(private router: Router) {}

  redirect() {
    this.router.navigate(['/theme-pages/form-management']);
  }
}
