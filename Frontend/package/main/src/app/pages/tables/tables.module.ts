import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';
import { NgApexchartsModule } from 'ng-apexcharts';

// icons
import { TablerIconsModule } from 'angular-tabler-icons';
import * as TablerIcons from 'angular-tabler-icons/icons';

import { TablesRoutes } from './tables.routing';

// tables components
import { AppMixTableComponent } from './mix-table/mix-table.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { ExpandedDataModalComponent } from './mix-table/expand-data-modal/expand-data-modal.component';
import { FormDialogComponent } from './mix-table/expand-form-dialog/expand-form-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(TablesRoutes),
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    TablerIconsModule.pick(TablerIcons),
    NgApexchartsModule,
    MatExpansionModule,
  ],
  declarations: [
    AppMixTableComponent,
    ExpandedDataModalComponent,
    FormDialogComponent,
  ],
})
export class TablesModule {}
