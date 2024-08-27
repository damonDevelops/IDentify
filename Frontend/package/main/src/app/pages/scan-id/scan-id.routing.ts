import { Routes } from '@angular/router';

// tables
import { CommonScanIdComponent } from './common-scan-id.component';
import { DesktopQrCodeScanIdComponent } from './qr-code-scan-id/qr-code-scan-id.component';

export const GablesRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'app-common-scan-id',
        component: CommonScanIdComponent,
        data: {
          title: 'Common Scan ID',
          urls: [
            { title: 'Dashboard', url: '/dashboards/dashboard1' },
            { title: 'Scan a New Card' },
          ],
        },
      },
      {
        path: 'desktop-qr-code-scan-id',
        component: DesktopQrCodeScanIdComponent,
        data: {
          title: 'Capture via QR',
          urls: [
            { title: 'Dashboard', url: '/dashboards/dashboard1' },
            { title: 'Scan a QR Code' },
          ],
        },
      },
    ],
  },
];
