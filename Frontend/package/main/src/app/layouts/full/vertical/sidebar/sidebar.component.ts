import { Component, OnInit } from '@angular/core';
import { NavService } from 'src/app/_services/nav.service';
import { NavItem } from './nav-item/nav-item'; // Import your NavItem type/interface
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { navItems } from './sidebar-data';
import { CommonModule } from '@angular/common';
import { NgScrollbarModule } from 'ngx-scrollbar';
import { TablerIconsModule } from 'angular-tabler-icons';
import { MaterialModule } from 'src/app/material.module';
import { RouterModule } from '@angular/router';
import { AppNavItemComponent } from './nav-item/nav-item.component';
import { AuthoritiesService } from 'src/app/_services/authorities/authorities.service';
@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    NgScrollbarModule,
    TablerIconsModule,
    MaterialModule,
    RouterModule,
    AppNavItemComponent,
    CommonModule,
  ],
  templateUrl: './sidebar.component.html',
})
export class SidebarComponent implements OnInit {
  navopt = this.navService.showClass;
  filteredNavItems: NavItem[] = [];

  constructor(
    public navService: NavService,
    private authoritiesService: AuthoritiesService,
  ) {}

  ngOnInit(): void {
    this.authoritiesService.companyPermissions$.subscribe((permissions) => {
      // Assuming 'requiredAuthorities' is still present in navItems
      this.filteredNavItems = navItems.filter((item) => {
        if (!item.requiredAuthorities) {
          return true;
        }
        return item.requiredAuthorities.every((auth) =>
          permissions.includes(auth),
        );
      });
    });
  }
}
