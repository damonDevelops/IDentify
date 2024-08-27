import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ApiService } from 'src/app/_services/api/api.service';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-update-email-dialog',
  templateUrl: './update-email-dialog.component.html',
  styleUrls: ['./update-email-dialog.component.scss'],
})
export class UpdateEmailDialogComponent {
  emailForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<UpdateEmailDialogComponent>,
    private fb: FormBuilder,
    private apiService: ApiService,
    private authService: AuthenticationService,
    private snackBar: MatSnackBar,
  ) {
    this.emailForm = this.fb.group(
      {
        newEmail: [
          '',
          [
            Validators.required,
            Validators.pattern(
              /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/,
            ),
          ],
        ],
        confirmNewEmail: ['', [Validators.required]],
      },
      { validator: this.emailsMatchValidator },
    );
  }

  onCloseClick(): void {
    this.dialogRef.close();
  }

  onSubmitClick(): void {
    const newEmail = this.emailForm.get('newEmail')!.value;
    const confirmNewEmail = this.emailForm.get('confirmNewEmail')!.value;

    if (newEmail === confirmNewEmail) {
      this.apiService.updateUserEmail(newEmail).subscribe(
        (result) => {
          //close the dialog
          this.dialogRef.close();

          //log the user out
          this.authService.logout();

          //give success message
          this.snackBar.open('Successfully Updated Email', 'Close', {
            duration: 5000, // 5 seconds
          });
        },
        (error) => {
          if (error.status === 409) {
            this.snackBar.open(
              'New password matches email, enter a new email to update.',
              'Close',
              {
                duration: 5000, // 5 seconds
              },
            );
          } else {
            this.snackBar.open(
              'Sorry, we can not update your email right now, try again later.',
              'Close',
              {
                duration: 5000, // 5 seconds
              },
            );
          }
        },
      );
    } else {
      this.emailForm.setErrors({ emailMismatch: true });
    }
  }

  // Custom validator function to check if emails match
  private emailsMatchValidator(
    control: AbstractControl,
  ): { [key: string]: boolean } | null {
    const newEmail = control.get('newEmail')!.value;
    const confirmNewEmail = control.get('confirmNewEmail')!.value;

    return newEmail === confirmNewEmail ? null : { emailMismatch: true };
  }
}
