import { Routes } from '@angular/router';

// theme pages
import { AccountSettingComponent } from './account-setting/account-setting.component';
import { CompanyManagementComponent } from './company-management/company-management/company-management.component';
import { FormManagementComponent } from './form-management/form-management.component';
import { AuthGuard } from 'src/app/auth.guard';

export const ThemePagesRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'account-setting',
        component: AccountSettingComponent,
        data: {
          title: 'Account Setting',
          urls: [
            { title: 'Dashboard', url: '/dashboards/dashboard1' },
            { title: 'Account Setting' },
          ],
        },
      },
      {
        path: 'company-management',
        component: CompanyManagementComponent,
        data: {
          title: 'Company Management',
          urls: [
            { title: 'Dashboard', url: '/dashboards/dashboard1' },
            { title: 'Company Management' },
          ],
        },
      },
      {
        path: 'form-management',
        component: FormManagementComponent,
        canActivate: [AuthGuard],
        data: {
          title: 'Form Management',
          urls: [
            { title: 'Dashboard', url: '/dashboards/dashboard1' },
            { title: 'Form Management' },
          ],
        },
      },
    ],
  },
];
