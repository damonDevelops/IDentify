import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';
import { NgApexchartsModule } from 'ng-apexcharts';
import {
  MatProgressSpinnerModule,
  MatSpinner,
} from '@angular/material/progress-spinner';
import { SharedModule } from 'src/app/shared/shared.module';
// icons
import { TablerIconsModule } from 'angular-tabler-icons';
import * as TablerIcons from 'angular-tabler-icons/icons';
import { QRCodeModule } from 'angularx-qrcode';

import { GablesRoutes } from './scan-id.routing';

// tables components
import { AppScanIdComponent } from './desktop-scan-id/scan-id.component';
import { MobileScanIdComponent } from './mobile-scan-id/mobile-scan-id.component';
import { CommonScanIdComponent } from './common-scan-id.component';
import { DesktopQrCodeScanIdComponent } from './qr-code-scan-id/qr-code-scan-id.component';
import { FailedJobsDialogComponent } from './failed-jobs-dialog/failed-jobs-dialog.component';
import { NoCollectionPointsDialogComponent } from './qr-code-scan-id/no-collection-points-dialog/no-collection-points-dialog.component';
@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(GablesRoutes),
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    TablerIconsModule.pick(TablerIcons),
    QRCodeModule,
    NgApexchartsModule,
    MatProgressSpinnerModule,
    SharedModule,
  ],
  declarations: [
    AppScanIdComponent,
    MobileScanIdComponent,
    CommonScanIdComponent,
    DesktopQrCodeScanIdComponent,
    FailedJobsDialogComponent,
    NoCollectionPointsDialogComponent,
  ],
})
export class GablesModule {}
