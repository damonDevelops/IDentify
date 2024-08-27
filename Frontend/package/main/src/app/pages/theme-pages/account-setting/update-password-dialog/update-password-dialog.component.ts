import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from 'src/app/_services/api/api.service';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-update-password-dialog',
  templateUrl: './update-password-dialog.component.html',
  styleUrls: ['./update-password-dialog.component.scss'],
})
export class UpdatePasswordDialogComponent {
  passwordForm: FormGroup;
  hide = true;
  hide2 = true;
  hide3 = true;

  anyRequirementsNotMet: boolean = false;

  // Regular expression to match a special character
  specialCharacterPattern = /[!@#$%^&*()_+[\]{};':"\\|,.<>?]/;
  numberPattern = /\d{4,}/;

  // Track whether each requirement is met
  hasSpecialCharacter = false;
  hasMinNumbers = false;
  hasMinLength = false;
  hasMaxLength = false;

  constructor(
    public dialogRef: MatDialogRef<UpdatePasswordDialogComponent>,
    private apiService: ApiService,
    private fb: FormBuilder,
    private authService: AuthenticationService,
    private snackBar: MatSnackBar,
  ) {
    this.passwordForm = this.fb.group(
      {
        currentPassword: ['', Validators.required],
        newPassword: [
          '',
          [
            Validators.required,
            Validators.minLength(7),
            Validators.maxLength(30),
            Validators.pattern(this.specialCharacterPattern), // At least one special character
            Validators.pattern(this.numberPattern), // Minimum of 4 numbers
          ],
        ],
        confirmNewPassword: ['', Validators.required],
      },
      {
        validators: this.matchingPasswordsValidator,
      },
    );

    this.passwordForm.get('newPassword')!.valueChanges.subscribe((value) => {
      this.updatePasswordRequirements(value);
    });

    // Update the requirement properties and check if any requirement is not met
    this.passwordForm.get('newPassword')!.valueChanges.subscribe((value) => {
      this.updatePasswordRequirements(value);
      this.anyRequirementsNotMet =
        !this.hasSpecialCharacter ||
        !this.hasMinNumbers ||
        !this.hasMinLength ||
        !this.hasMaxLength;
    });
  }

  private matchingPasswordsValidator(control: FormGroup) {
    const newPassword = control.get('newPassword')!.value;
    const confirmNewPassword = control.get('confirmNewPassword')!.value;

    if (newPassword === confirmNewPassword) {
      return null;
    } else {
      control.get('confirmNewPassword')!.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
  }

  close(): void {
    this.dialogRef.close();
  }

  onSubmitClick(): void {
    const currentPassword = this.passwordForm.get('currentPassword')!.value;
    const newPassword = this.passwordForm.get('newPassword')!.value;

    this.apiService.updateUserPassword(currentPassword, newPassword).subscribe(
      (result) => {
        //close the dialog
        this.dialogRef.close();

        //log the user out
        this.authService.logout();

        //give success message
        this.snackBar.open('Successfully Updated Password', 'Close', {
          duration: 5000, // 5 seconds
        });
      },
      (error) => {
        if (error.status === 409) {
          this.snackBar.open(
            'Old password and new password can not match',
            'Close',
            {
              duration: 5000, // 5 seconds
            },
          );
        } else if (error.status === 400) {
          this.snackBar.open(
            'Current password incorrect, please try again',
            'Close',
            {
              duration: 5000, // 5 seconds
            },
          );
        }
      },
    );
  }

  updatePasswordRequirements(value: string): void {
    this.hasSpecialCharacter = this.specialCharacterPattern.test(value);
    this.hasMinNumbers = this.numberPattern.test(value);
    this.hasMinLength = value.length >= 7;
    this.hasMaxLength = value.length <= 30;
  }
}
