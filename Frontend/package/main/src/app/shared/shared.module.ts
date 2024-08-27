import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CheckingIpsDialogComponent } from './components/checking-ips-dialog/checking-ips-dialog.component';
import { ConsentDialogComponent } from './components/consent-dialog/consent-dialog.component';
import { RedirectDialogComponent } from './components/redirect-dialog/redirect-dialog.component';
import { TimeoutDialogComponent } from './components/timeout-dialog/timeout-dialog.component';

import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import {
  MatProgressSpinner,
  MatProgressSpinnerModule,
} from '@angular/material/progress-spinner';
import { MaterialModule } from 'src/app/material.module';
import { NgApexchartsModule } from 'ng-apexcharts';

// icons
import { TablerIconsModule } from 'angular-tabler-icons';
import * as TablerIcons from 'angular-tabler-icons/icons';
import { ExpandDataModalMobileComponent } from './expand-data-modal-mobile/expand-data-modal-mobile.component';
import { WelcomeDialogComponent } from './components/welcome-dialog/welcome-dialog.component';

@NgModule({
  declarations: [
    CheckingIpsDialogComponent,
    ConsentDialogComponent,
    RedirectDialogComponent,
    TimeoutDialogComponent,
    ExpandDataModalMobileComponent,
    WelcomeDialogComponent,
    // ... other shared components
  ],
  imports: [
    CommonModule,
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
    // Any other modules that are required by the shared components
  ],
  exports: [
    CheckingIpsDialogComponent,
    ConsentDialogComponent,
    RedirectDialogComponent,
    TimeoutDialogComponent,
    // ... other shared components
  ],
})
export class SharedModule {}
