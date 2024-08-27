import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';
import { MatNativeDateModule } from '@angular/material/core';

// icons
import { TablerIconsModule } from 'angular-tabler-icons';
import * as TablerIcons from 'angular-tabler-icons/icons';

import { QRCodeModule } from 'angularx-qrcode';

import { ThemePagesRoutes } from './theme-pages.routing';

// theme pages
import { AccountSettingComponent } from './account-setting/account-setting.component';
import { CompanyManagementComponent } from './company-management/company-management/company-management.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { FormManagementComponent } from './form-management/form-management.component';
import { CreateNewFormDialogComponent } from './form-management/create-new-form-dialog/create-new-form-dialog.component';
import { HowDoesThisWorkDialogComponent } from './form-management/how-does-this-work-dialog/how-does-this-work-dialog.component';
import { FormsTableComponent } from './form-management/forms-table/forms-table.component';
import { UpdateFormDialogComponent } from './form-management/update-form-dialog/update-form-dialog.component';
import { DeleteFormDialogComponent } from './form-management/delete-form-dialog/delete-form-dialog.component';
import { UpdateEmailDialogComponent } from './account-setting/update-email-dialog/update-email-dialog.component';
import { UpdatePasswordDialogComponent } from './account-setting/update-password-dialog/update-password-dialog.component';
import { CreateCompanyComponent } from './company-management/company-management/create-company/create-company.component';
import { InviteUsersComponent } from './company-management/company-management/invite-users/invite-users.component';
import { ViewUsersComponent } from './company-management/company-management/view-users/view-users.component';
import { CollectionPointComponent } from './company-management/company-management/collection-point/collection-point.component';
import { AddUserDialogComponent } from './company-management/company-management/collection-point/add-user-dialog/add-user-dialog/add-user-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(ThemePagesRoutes),
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    TablerIconsModule.pick(TablerIcons),
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    QRCodeModule,
  ],
  declarations: [
    AccountSettingComponent,
    CompanyManagementComponent,
    FormManagementComponent,
    CreateNewFormDialogComponent,
    HowDoesThisWorkDialogComponent,
    FormsTableComponent,
    UpdateFormDialogComponent,
    DeleteFormDialogComponent,
    UpdateEmailDialogComponent,
    UpdatePasswordDialogComponent,
    CreateCompanyComponent,
    InviteUsersComponent,
    ViewUsersComponent,
    CollectionPointComponent,
    AddUserDialogComponent,
  ],
})
export class ThemePagesModule {}
