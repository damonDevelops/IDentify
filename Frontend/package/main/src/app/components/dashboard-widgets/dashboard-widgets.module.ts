import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

// icons
import { TablerIconsModule } from 'angular-tabler-icons';
import { IconIdBadge2, IconQrcode } from 'angular-tabler-icons/icons';
import { IconHistory } from 'angular-tabler-icons/icons';
import { IconFileDescription } from 'angular-tabler-icons/icons';
import { IconSettings } from 'angular-tabler-icons/icons';
import { IconBuilding } from 'angular-tabler-icons/icons';

//widget components
import { AppScanIdRedirectComponent } from './scan-id-redirect/scan-id-redirect.component';
import { AppQRCodeGenerationRedirectComponent } from './qr-code-generation-redirect/qr-code-generation-redirect.component';
import { AppScanHistoryRedirectWidget } from './scan-history-redirect/scan-history.component';
import { AppFormManagementRedirectComponent } from './form-management-redirect/form-management-redirect.component';
import { AppAccountSettingsRedirectComponent } from './account-settings-redirect/account-settings-redirect.component';
import { AppCompanyManagementRedirectComponent } from './company-management-redirect/company-management-redirect.component';
//selectable icons
const TablerIcons = {
  IconIdBadge2,
  IconQrcode,
  IconHistory,
  IconBuilding,
  IconFileDescription,
  IconSettings,
};

@NgModule({
  declarations: [
    AppScanIdRedirectComponent,
    AppQRCodeGenerationRedirectComponent,
    AppScanHistoryRedirectWidget,
    AppFormManagementRedirectComponent,
    AppCompanyManagementRedirectComponent,
    AppAccountSettingsRedirectComponent,
  ],
  imports: [
    RouterModule,
    MatCardModule,
    MatButtonModule,
    TablerIconsModule.pick(TablerIcons),
  ],
  exports: [
    AppScanIdRedirectComponent,
    AppQRCodeGenerationRedirectComponent,
    AppScanHistoryRedirectWidget,
    AppFormManagementRedirectComponent,
    AppCompanyManagementRedirectComponent,
    AppAccountSettingsRedirectComponent,
    MatCardModule,
    MatButtonModule,
    TablerIconsModule,
  ],
})
export class DashboardWidgetsModule {}
