import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DatePipe } from '@angular/common';
import { CommonModule } from '@angular/common';

// map
// import { DxVectorMapModule, DxPieChartModule } from 'devextreme-angular';

//dashboard widgets
import { DashboardWidgetsModule } from 'src/app/components/dashboard-widgets/dashboard-widgets.module';

//routing
import { DashboardsRoutes } from './dashboards.routing';
import { AppDashboard1Component } from './dashboard1/dashboard1.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(DashboardsRoutes),
    DashboardWidgetsModule,
  ],
  declarations: [AppDashboard1Component],
  providers: [DatePipe],
})
export class DashboardsModule {}
