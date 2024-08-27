import { Component } from '@angular/core';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { AuthoritiesService } from 'src/app/_services/authorities/authorities.service';
@Component({
  selector: 'app-company-management',
  templateUrl: './company-management.component.html',
  styleUrls: ['./company-management.component.scss'],
})
export class CompanyManagementComponent {
  hasCompany: boolean = sessionStorage.getItem('hasCompany') === 'true';
  editCompany = false;

  constructor(
    private authService: AuthenticationService,
    private authoritiesService: AuthoritiesService,
  ) {}

  ngOnInit() {
    // Subscribe to the authorities and update flags based on specific permissions
    this.authoritiesService.companyPermissions$.subscribe((permissions) => {
      this.editCompany = permissions.includes('EDIT_USERS');
    });
  }
}
