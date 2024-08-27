import { BreakpointObserver, MediaMatcher } from '@angular/cdk/layout';
import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { Subscription } from 'rxjs';
import { MatSidenav } from '@angular/material/sidenav';
import { CoreService } from 'src/app/_services/core.service';
import { AppSettings } from 'src/app/app.config';
import { navItems } from './vertical/sidebar/sidebar-data';
import { NavService } from 'src/app/_services/nav.service';
import { NgScrollbarModule } from 'ngx-scrollbar';
import { HeaderComponent } from './vertical/header/header.component';
import { AppHorizontalHeaderComponent } from './horizontal/header/header.component';
import { AppHorizontalSidebarComponent } from './horizontal/sidebar/sidebar.component';
import { SidebarComponent } from './vertical/sidebar/sidebar.component';
import { AppBreadcrumbComponent } from './shared/breadcrumb/breadcrumb.component';
import { CustomizerComponent } from './shared/customizer/customizer.component';
import { MaterialModule } from 'src/app/material.module';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AppNavItemComponent } from './vertical/sidebar/nav-item/nav-item.component';
import { NavItem } from './vertical/sidebar/nav-item/nav-item';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { IntroService } from 'src/app/_services/intro-js/intro-js.service';
import { AuthoritiesService } from 'src/app/_services/authorities/authorities.service';

const MOBILE_VIEW = 'screen and (max-width: 768px)';
const TABLET_VIEW = 'screen and (min-width: 769px) and (max-width: 1024px)';
const MONITOR_VIEW = 'screen and (min-width: 1024px)';

@Component({
  selector: 'app-full',
  templateUrl: './full.component.html',
  standalone: true,
  imports: [
    NgScrollbarModule,
    HeaderComponent,
    AppHorizontalHeaderComponent,
    AppHorizontalSidebarComponent,
    SidebarComponent,
    AppBreadcrumbComponent,
    CustomizerComponent,
    MaterialModule,
    RouterModule,
    CommonModule,
    AppNavItemComponent,
  ],
  styleUrls: [],
  encapsulation: ViewEncapsulation.None,
})
export class FullComponent implements OnInit {
  navItems = navItems;
  @ViewChild('leftsidenav')
  public sidenav: MatSidenav;
  filteredNavItems: NavItem[] = []; // Add this property

  //get options from service
  options = this.settings.getOptions();
  navopt = this.navService.showClass;
  private layoutChangesSubscription = Subscription.EMPTY;
  private isMobileScreen = false;
  private isContentWidthFixed = true;
  private isCollapsedWidthFixed = false;
  private htmlElement!: HTMLHtmlElement;

  get isOver(): boolean {
    return this.isMobileScreen;
  }

  constructor(
    private authService: AuthenticationService,
    private settings: CoreService,
    private mediaMatcher: MediaMatcher,
    private navService: NavService,
    private breakpointObserver: BreakpointObserver,
    private introService: IntroService,
    private authoritiesService: AuthoritiesService,
  ) {
    this.htmlElement = document.querySelector('html')!;
    this.layoutChangesSubscription = this.breakpointObserver
      .observe([MOBILE_VIEW, TABLET_VIEW, MONITOR_VIEW])
      .subscribe((state) => {
        // SidenavOpened must be reset true when layout changes
        this.options.sidenavOpened = true;
        this.isMobileScreen = state.breakpoints[MOBILE_VIEW];
        if (this.options.sidenavCollapsed == false) {
          this.options.sidenavCollapsed = state.breakpoints[TABLET_VIEW];
        }
        this.isContentWidthFixed = state.breakpoints[MONITOR_VIEW];
      });

    // Initialize project theme with options
    this.receiveOptions(this.options);
  }

  ngOnInit(): void {
    this.applyNavItemsFilter();

    // Add a step pointing to an element with a specific id.
    this.introService.addStepWithPosition(
      {
        element: '#step3', // Use the ID selector to target the element.
        title: 'Sidebar',
        intro:
          'Here we have the sidebar panel, to give you detailed access of all areas of the dashboard. Lets show you how to capture an ID card for your system.',
        position: 'right',
      },
      3,
    );
  }

  ngOnDestroy() {
    this.layoutChangesSubscription.unsubscribe();
  }

  toggleCollapsed() {
    this.isContentWidthFixed = false;
    this.options.sidenavCollapsed = !this.options.sidenavCollapsed;
    this.resetCollapsedState();
  }

  resetCollapsedState(timer = 400) {
    setTimeout(() => this.settings.setOptions(this.options), timer);
  }

  onSidenavClosedStart() {
    this.isContentWidthFixed = false;
  }

  onSidenavOpenedChange(isOpened: boolean) {
    this.isCollapsedWidthFixed = !this.isOver;
    this.options.sidenavOpened = isOpened;
    this.settings.setOptions(this.options);
  }

  receiveOptions(options: AppSettings): void {
    this.options = options;
    this.toggleDarkTheme(options);
  }

  toggleDarkTheme(options: AppSettings) {
    if (options.theme === 'dark') {
      this.htmlElement.classList.add('dark-theme');
      this.htmlElement.classList.remove('light-theme');
    } else {
      this.htmlElement.classList.remove('dark-theme');
      this.htmlElement.classList.add('light-theme');
    }
  }

  private applyNavItemsFilter() {
    this.authoritiesService.companyPermissions$.subscribe((permissions) => {
      const hasCompany = sessionStorage.getItem('hasCompany') === 'true';

      // Assuming 'requiredAuthorities' is still present in navItems
      this.filteredNavItems = navItems.filter((item) => {
        if (!item.requiredAuthorities) {
          return true;
        }

        if (hasCompany || hasCompany === null) {
          // If 'hasCompany' is true or not found, proceed with the regular authority-based filtering
          return item.requiredAuthorities.every((auth) =>
            permissions.includes(auth),
          );
        }

        return false;
      });
    });
  }
}
