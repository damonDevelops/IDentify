import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-qr-redirect-component',
  templateUrl: './qr-code-generation-redirect.component.html',
})
export class AppQRCodeGenerationRedirectComponent {
  constructor(private router: Router) {}

  redirect() {
    this.router.navigate(['/scan-id/desktop-qr-code-scan-id']);
  }
}
