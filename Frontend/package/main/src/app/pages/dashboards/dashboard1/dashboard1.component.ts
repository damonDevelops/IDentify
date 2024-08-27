import { Component, OnInit } from '@angular/core';
import { AuthoritiesService } from 'src/app/_services/authorities/authorities.service';
import { IntroService } from 'src/app/_services/intro-js/intro-js.service';

@Component({
  selector: 'app-dashboard1',
  templateUrl: './dashboard1.component.html',
})
export class AppDashboard1Component implements OnInit {
  userHasRequiredAuthorities = false;
  userHasViewCompanyPermission = false;
  hasCompany = false;

  constructor(
    private authoritiesService: AuthoritiesService,
    private introService: IntroService,
  ) {}

  ngOnInit(): void {
    if (sessionStorage.getItem('hasCompany') === 'true') {
      this.hasCompany = true;
    } else {
      this.hasCompany = false;
    }

    this.checkUserAuthorities();
    this.configureIntro();
  }

  configureIntro(): void {
    this.introService.setOptions({
      tooltipClass: 'customTooltip',
      highlightClass: 'customHighlight',
      exitOnOverlayClick: false,
      disableInteraction: false,
    });

    this.introService.addStepWithPosition(
      {
        intro:
          'Welcome to IDentify, you are moments away from revolutionising the way you interact with customers. First step is to throw those old paper forms into the garbage.',
        title: 'Welcome to IDentify',
      },
      1, // Specify the position for Component A's step
    );

    // Add a step pointing to an element with a specific id.
    this.introService.addStepWithPosition(
      {
        element: '#step1', // Use the ID selector to target the element.
        title: 'Quick Access',
        intro:
          'Here we have your quick access panels. From here, you can access frequent features of the dashboard such as scanning a new card, updating your QR code collection points as well as many other features.',
      },
      2,
    );

    //this.introService.start();

    // Add more steps as needed for this component.
  }

  private checkUserAuthorities(): void {
    this.authoritiesService.companyPermissions$.subscribe((permissions) => {
      if (permissions) {
        // Check if the user has the required authorities for QR redirect
        const requiredAuthoritiesForQR = [
          'EDIT_COMPANY',
          'VIEW_COMPANY',
          'EDIT_COLLECTION',
        ];
        this.userHasRequiredAuthorities = requiredAuthoritiesForQR.every(
          (auth) => permissions.includes(auth),
        );

        // Check if the user has VIEW_COMPANY permission
        this.userHasViewCompanyPermission =
          permissions.includes('VIEW_COMPANY');
      }
    });
  }
}
