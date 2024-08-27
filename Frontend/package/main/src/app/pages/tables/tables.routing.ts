import { Routes } from '@angular/router';

// tables
import { AppMixTableComponent } from './mix-table/mix-table.component';

export const TablesRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'scan-history',
        component: AppMixTableComponent,
        data: {
          title: 'Scan History',
          urls: [
            { title: 'Dashboard', url: '/dashboards/dashboard1' },
            { title: 'Scan History' },
          ],
        },
      },
    ],
  },
];
