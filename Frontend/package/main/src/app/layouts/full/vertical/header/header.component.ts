// header.component.ts
import {
  Component,
  Output,
  EventEmitter,
  Input,
  ViewEncapsulation,
  OnInit,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RouterModule } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';
import { TablerIconsModule } from 'angular-tabler-icons';
import { NgScrollbarModule } from 'ngx-scrollbar';
import { BrandingComponent } from '../sidebar/branding.component';
import { NgFor, NgIf } from '@angular/common';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { Router } from '@angular/router';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { IntroService } from 'src/app/_services/intro-js/intro-js.service';
import { Company } from 'src/app/_models/company';
import { Subject, takeUntil } from 'rxjs';

interface profiledd {
  id: number;
  img: string;
  title: string;
  subtitle: string;
  link: string;
}

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    RouterModule,
    NgScrollbarModule,
    TablerIconsModule,
    MaterialModule,
    BrandingComponent,
    NgFor,
    NgIf,
  ],
  templateUrl: './header.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  @Input() showToggle = true;
  @Input() toggleChecked = false;
  @Output() toggleMobileNav = new EventEmitter<void>();
  @Output() toggleMobileFilterNav = new EventEmitter<void>();
  @Output() toggleCollapsed = new EventEmitter<void>();
  private ngUnsubscribe = new Subject<void>();

  userName: string | null = null;
  email: string | null = null;
  companies: Company[] = [];
  activeCompany: Company | null = null;

  constructor(
    public dialog: MatDialog,
    private router: Router,
    private authenticationService: AuthenticationService,
    private companyService: CompanyServiceService, // Inject CompanyService
    private introService: IntroService,
  ) {}

  ngOnInit() {
    // Subscribe to the companies and activeCompany observables from the CompanyService
    this.companyService
      .getCompanies()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((companies) => {
        this.companies = companies;
      });

    this.companyService.getActiveCompany().subscribe((activeCompany) => {
      this.activeCompany = activeCompany;
    });

    this.introService.addStepWithPosition(
      {
        element: '#step4', // Use the ID selector to target the element.
        title: 'Account Settings',
        intro:
          'Here is your profile, where you can access your account settings, switch between companies, and log out.',
      },
      4,
    );

    this.introService.addStepWithPosition(
      {
        element: '#step5', // Use the ID selector to target the element.
        title: 'Support',
        intro:
          'To view this again, press this button. Thanks for learning about IDentify; now go out and conquer with seamless customer interactions.',
      },
      5,
    );
  }

  // Select a company and update the active company in the CompanyService
  selectCompany(company: Company) {
    this.companyService.setActiveCompany(company);
    //window.location.reload();
  }

  open() {
    this.companyService.clearCompanies();

    this.authenticationService.logout();
  }

  openSettings() {
    this.router.navigate(['/']);
  }

  profiledd: profiledd[] = [
    {
      id: 1,
      img: '/assets/images/svgs/icon-account.svg',
      title: 'My Profile',
      subtitle: 'Account Settings',
      link: './theme-pages/account-setting',
    },
  ];

  showTutorial() {
    this.router.navigate(['./dashboards/dashboard1']);
    this.introService.start();
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
