import { NavItem } from './nav-item/nav-item';

export const navItems: NavItem[] = [
  {
    navCap: 'Home',
  },
  {
    displayName: 'Dashboard',
    iconName: 'home',
    route: '/dashboards/dashboard1',
  },
  {
    displayName: 'Capture New Card',
    iconName: 'camera',
    route: 'scan-id/app-common-scan-id',
    requiredAuthorities: ['VIEW_COMPANY'],
  },
  {
    displayName: 'Capture via QR',
    iconName: 'scan',
    route: 'scan-id/desktop-qr-code-scan-id',
    requiredAuthorities: ['VIEW_COMPANY'],
  },
  {
    displayName: 'Scan History',
    iconName: 'table',
    route: 'tables/scan-history',
    requiredAuthorities: ['VIEW_COMPANY'],
  },
  {
    navCap: 'Company',
  },
  {
    displayName: 'Company Management',
    iconName: 'point',
    route: 'theme-pages/company-management',
  },
  {
    displayName: 'Form Management',
    iconName: 'point',
    route: 'theme-pages/form-management',
    requiredAuthorities: ['VIEW_COMPANY'],
  },
  {
    navCap: 'Other',
  },
  {
    displayName: 'Account Settings',
    iconName: 'user-circle',
    route: 'theme-pages/account-setting',
  },
];
