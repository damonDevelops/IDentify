import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-account-settings',
  templateUrl: './account-settings-redirect.component.html',
})
export class AppAccountSettingsRedirectComponent {
  imageSrc = './assets/images/svgs/id-card.svg';
  constructor(private router: Router) {}

  redirect() {
    this.router.navigate(['/theme-pages/account-setting']);
  }
}
