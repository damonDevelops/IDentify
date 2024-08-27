import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { FormBuilder } from '@angular/forms';
import {
  MatDialog,
  MatDialogRef,
  MatDialogModule,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import {
  MatProgressSpinner,
  MatProgressSpinnerModule,
} from '@angular/material/progress-spinner';
//services
import { CryptoUtilService } from 'src/app/_services/crypto-util/crypto-util.service';
import { CoreService } from 'src/app/_services/core.service';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { ApiService } from 'src/app/_services/api/api.service';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { WelcomeDialogComponent } from 'src/app/shared/components/welcome-dialog/welcome-dialog.component';
import { IntroService } from 'src/app/_services/intro-js/intro-js.service';
import { AuthoritiesService } from 'src/app/_services/authorities/authorities.service';
@Component({
  selector: 'app-side-login',
  templateUrl: './side-login.component.html',
})
export class AppSideLoginComponent {
  options = this.settings.getOptions();
  form!: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  firstLogin = false;

  constructor(
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private authenticationService: AuthenticationService,
    private settings: CoreService,
    private router: Router,
    private apiService: ApiService,
    private cryptoUtilService: CryptoUtilService,
    private companyService: CompanyServiceService,
    private introService: IntroService,
    private authoritiesService: AuthoritiesService
  ) {}

  ngOnInit() {
    this.form = new FormGroup({
      uname: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    });
  }

  openDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string
  ): void {
    this.dialog.open(DialogAnimationsExampleDialog, {
      width: '250px',
      enterAnimationDuration,
      exitAnimationDuration,
    });
  }

  openWelcomeDialog(): void {
    const dialogRef = this.dialog.open(WelcomeDialogComponent, {
      width: '300px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.introService.start();
    });
  }

  get f() {
    return this.form.controls;
  }

  submit() {
    this.submitted = true;
    this.error = '';
    this.loading = true;

    this.authenticationService
      .login(this.form.get('uname')!.value, this.form.get('password')!.value)
      .pipe(first())
      .subscribe({
        next: (result) => {
          const user = result;
          const firstTimeLogin = user.firstTimeLogin !== undefined;

          if (firstTimeLogin) {
            this.firstLogin = true;
          }

          this.cryptoUtilService.storeToSession(
            'password',
            this.form.get('password')!.value
          );

          // Fetch user data using ApiService
          this.apiService.fetchUser().subscribe(
            (userData: any) => {
              //console.log('Fetched user data:', userData);

              if (userData.companyList && userData.companyList.length > 0) {
                // Set hasCompany to true
                sessionStorage.setItem('hasCompany', 'true');

                // Fetch the list of companies
                this.companyService.fetchCompanies();
                //console.log('Fetching companies...');

                // Get the return URL
                const returnUrl =
                  this.route.snapshot.queryParams['returnUrl'] ||
                  '/dashboards/dashboard1';

                // Check if lastCompanyId is in session storage
                const lastCompanyId = localStorage.getItem('lastCompanyId');

                if (lastCompanyId) {
                  // Check if there is a matching company for the lastCompanyId
                  this.companyService.getCompanies().subscribe((companies) => {
                    const company = companies.find(
                      (c) => c.id === lastCompanyId
                    );

                    if (company) {
                      console.log('Matching company found:', company);
                      this.companyService.setActiveCompany(company);
                    } else {
                      console.log(
                        'No matching company found for lastCompanyId:',
                        lastCompanyId
                      );
                      // Set the first company as active
                      const firstCompany = userData.companyList[0];
                      if (firstCompany) {
                        console.log(
                          'Setting the first company as active:',
                          firstCompany
                        );
                        this.companyService.setActiveCompany(firstCompany);
                      }
                    }
                  });
                } else {
                  // If no lastCompanyId, set the first company as active
                  const firstCompany = userData.companyList[0];
                  if (firstCompany) {
                    console.log(
                      'No lastCompanyId found. Setting the first company as active:',
                      firstCompany
                    );
                    this.companyService.setActiveCompany(firstCompany);
                  }
                }

                //we initiate the authorities service by using the active company to create an observable subscribable
                //set of authorities. This is what everyone will subscribe to

                // After setting the active company, fetch and store company permissions
                // After setting the active company, fetch and store company permissions
                this.companyService
                  .activeCompanyIsSet()
                  .subscribe((activeCompanyIsSet) => {
                    if (activeCompanyIsSet) {
                      this.companyService
                        .getActiveCompany()
                        .subscribe((activeCompany) => {
                          if (activeCompany) {
                            console.log(
                              'Active Company ID:',
                              activeCompany.id
                            ); // Log the id
                            this.authoritiesService.fetchCompanyPermissions(
                              activeCompany.id
                            );
                          }
                        });
                    }
                  });

                // Navigate to the return URL
                this.router.navigate([returnUrl]);
              } else {
                sessionStorage.setItem('hasCompany', 'false');
                // Handle the case when companyList is empty or not defined
              }

              if (firstTimeLogin) {
                this.router.navigate(['./dashboards/dashboard1']);
                this.openWelcomeDialog();
              } else {
                this.router.navigate(['./dashboards/dashboard1']);
              }
            },
            (error) => {
              console.log('Error fetching user data:', error);
            }
          );
        },
        error: (error) => {
          console.log(error);

          if (error.status == 401) {
            this.openDialog('0ms', '0ms');
            // alert("Incorrect username or password, please try again")
          }

          console.log('Error Status:', error.status);
          this.error = error;
          this.loading = false;
        },
      });
  }
}

@Component({
  selector: 'dialog-dialog',
  templateUrl: 'dialog-dialog.html',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatProgressSpinnerModule],
})
export class DialogAnimationsExampleDialog {
  constructor(public dialogRef: MatDialogRef<DialogAnimationsExampleDialog>) {}
}
