import { Component, Inject } from '@angular/core';
import { CoreService } from 'src/app/_services/core.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { environment } from 'src/environments/environment';
import {
  MatDialog,
  MatDialogRef,
  MatDialogModule,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

export interface DialogData {
  title: string;
  text: string;
}

@Component({
  selector: 'app-side-register',
  templateUrl: './side-register.component.html',
})
export class AppSideRegisterComponent {
  options = this.settings.getOptions();
  form!: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  successful_sign_in = false;

  emailControl = new FormControl('', [
    Validators.required,
    Validators.pattern(EMAIL_REGEX),
  ]);

  constructor(
    public dialog: MatDialog,
    private settings: CoreService,
    private authenticationService: AuthenticationService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.form = new FormGroup({
      fullName: new FormControl('', [Validators.required]),
      email: this.emailControl, // Use the emailControl for email field
    });
  }

  // Add a method to check if the email format is valid
  isEmailValid(): boolean {
    return this.emailControl.valid;
  }

  openDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
    dialogTitle: string,
    dialogText: string,
  ): void {
    const dialogRef = this.dialog.open(DialogAnimationsExampleDialog, {
      width: '250px',
      data: { title: dialogTitle, text: dialogText },
      enterAnimationDuration,
      exitAnimationDuration,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (this.successful_sign_in) {
        if (sessionStorage.getItem('hasCompany') === 'true') {
          this.router.navigate(['/dashboards/dashboard1']);
        } else {
          this.router.navigate(['/theme-pages/company-management']);
        }
      } else {
        this.router.navigate(['/authentication/side-login']);
      }
    });
  }

  get f() {
    return this.form.controls;
  }

  submit() {
    this.submitted = true;
    this.error = '';
    this.loading = true;

    // Call the registerV2 API method with the provided fullName and email
    this.authenticationService
      .registerV2(
        this.form.get('fullName')!.value,
        this.form.get('email')!.value,
      )
      .subscribe(
        (response) => {
          this.submitted = false;
          this.openDialog(
            '0ms',
            '0ms',
            'Verify Your Email Address',
            "Welcome to IDentify, we've sent you an email to verify your account and set your password. Once this is done you will be redirected to our sign in page!",
          );
        },
        (error) => {
          if (error.status == 500) {
            this.openDialog(
              '0ms',
              '0ms',
              'Invalid Sign Up',
              'Email account already in use, redirecting you to the login page.',
            );
            this.submitted = false;
          } else {
            this.openDialog(
              '0ms',
              '0ms',
              'Failed Sign Up',
              'We couldn not sign you up at this time, please try again later.',
            );
          }
          console.error('Registration error:', error);
          // Handle any error that occurs during the registration process
        },
      );
  }
}

@Component({
  selector: 'register-dialog',
  templateUrl: 'register-dialog.html',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})
export class DialogAnimationsExampleDialog {
  constructor(
    public dialogRef: MatDialogRef<DialogAnimationsExampleDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
  ) {}
}
