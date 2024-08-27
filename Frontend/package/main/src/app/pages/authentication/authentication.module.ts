import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';

// icons
import { TablerIconsModule } from 'angular-tabler-icons';
import * as TablerIcons from 'angular-tabler-icons/icons';

import { AuthenticationRoutes } from './authentication.routing';

import { AppBoxedForgotPasswordComponent } from './boxed-forgot-password/boxed-forgot-password.component';
import { AppBoxedLoginComponent } from './boxed-login/boxed-login.component';
import { AppBoxedRegisterComponent } from './boxed-register/boxed-register.component';
import { AppBoxedTwoStepsComponent } from './boxed-two-steps/boxed-two-steps.component';
import { AppErrorComponent } from './error/error.component';
import { AppMaintenanceComponent } from './maintenance/maintenance.component';
import { AppSideForgotPasswordComponent } from './side-forgot-password/side-forgot-password.component';
import { AppSideLoginComponent } from './side-login/side-login.component';
import { AppSideRegisterComponent } from './side-register/side-register.component';
import { AppSideTwoStepsComponent } from './side-two-steps/side-two-steps.component';
import {
  MatProgressSpinner,
  MatProgressSpinnerModule,
} from '@angular/material/progress-spinner';
import { MobileScanIdComponent } from './customer/mobile-scan-id/mobile-scan-id.component';
import { MaterialModule } from 'src/app/material.module';
import { NgApexchartsModule } from 'ng-apexcharts';
import { HowItWorksComponent } from './how-it-works/how-it-works.component';

import { SharedModule } from 'src/app/shared/shared.module';
import { DeactivatedCollectionPointDialogComponent } from './customer/mobile-scan-id/deactivated-collection-point-dialog/deactivated-collection-point-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AuthenticationRoutes),
    MatIconModule,
    MatCardModule,
    MatInputModule,
    MatCheckboxModule,
    MatDialogModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    FormsModule,
    ReactiveFormsModule,
    TablerIconsModule.pick(TablerIcons),
    MaterialModule,
    NgApexchartsModule,
    SharedModule,
  ],
  declarations: [
    AppBoxedForgotPasswordComponent,
    AppBoxedLoginComponent,
    AppBoxedRegisterComponent,
    AppBoxedTwoStepsComponent,
    AppErrorComponent,
    AppMaintenanceComponent,
    AppSideForgotPasswordComponent,
    AppSideLoginComponent,
    AppSideRegisterComponent,
    AppSideTwoStepsComponent,
    MobileScanIdComponent,
    HowItWorksComponent,
    DeactivatedCollectionPointDialogComponent,
  ],
})
export class AuthenticationModule {}
