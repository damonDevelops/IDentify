import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ApiService } from 'src/app/_services/api/api.service';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { UpdateEmailDialogComponent } from './update-email-dialog/update-email-dialog.component';
import { UpdatePasswordDialogComponent } from './update-password-dialog/update-password-dialog.component';

@Component({
  selector: 'app-account-setting',
  templateUrl: './account-setting.component.html',
  styleUrls: ['./account-setting.component.scss'],
})
export class AccountSettingComponent implements OnInit {
  email: string;

  constructor(
    private apiService: ApiService,
    private dialog: MatDialog,
    private authService: AuthenticationService,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
  ) {}

  ngOnInit() {
    this.fetchUserData();
  }

  private fetchUserData() {
    this.apiService.fetchUser().subscribe(
      (userData: any) => {
        this.email = userData.email;
      },
      (error) => {
        console.error('Error fetching user data:', error);
      },
    );
  }

  openEmailDialog() {
    const dialogRef = this.dialog.open(UpdateEmailDialogComponent, {
      width: 'auto',
      data: {},
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.updateUserData(result);
      }
    });
  }

  openPasswordDialog() {
    const dialogRef = this.dialog.open(UpdatePasswordDialogComponent, {
      width: '400px',
      data: {},
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.updateUserData(result);
      }
    });
  }

  private updateUserData(userData: any) {
    // Implement the API call to update user data with the provided userData
    // You can use the 'userData' object to send the necessary data to the API
    // Example:
    // this.apiService.updateUser(userData).subscribe(
    //   (response: any) => {
    //     // Handle the response from the API
    //     this.authService.logout();
    //   },
    //   (error) => {
    //     console.error('Error updating user data:', error);
    //   }
    // );
  }

  updateEmail() {}

  updatePassword() {}
}
