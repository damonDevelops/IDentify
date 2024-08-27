import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { BreakpointObserver } from '@angular/cdk/layout';
import { ApiService } from 'src/app/_services/api/api.service';
import { MatDialog } from '@angular/material/dialog';
import { UpdateFormDialogComponent } from '../update-form-dialog/update-form-dialog.component';
import { DeleteFormDialogComponent } from '../delete-form-dialog/delete-form-dialog.component';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { Subject, takeUntil } from 'rxjs';
export interface Form {
  id: string;
  company: any;
  fileName: any;
  description: any;
  data: any;
}

@Component({
  selector: 'app-forms-table',
  templateUrl: './forms-table.component.html',
  styleUrls: ['./forms-table.component.scss'],
})
export class FormsTableComponent {
  forms: Form[] = [];
  expandedFormId: string | null = null;
  private ngUnsubscribe = new Subject<void>();

  constructor(
    private apiService: ApiService,
    public dialog: MatDialog,
    private companyService: CompanyServiceService,
  ) {}

  ngOnInit() {
    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService.getForms(activeCompany.id).subscribe(
            (data: Form[]) => {
              this.forms = data;
            },
            (error) => {
              console.error('Error fetching data:', error);
            },
          );
        }
      });

    // Fetch data from the API
  }

  toggleExpansion(formId: string | null): void {
    this.expandedFormId = this.expandedFormId === formId ? null : formId;
  }

  openUpdateFormDialog(form: any): void {
    const dialogRef = this.dialog.open(UpdateFormDialogComponent, {
      data: { form: form }, // Pass the entire form object as data to the dialog
    });
  }

  openDeleteFormDialog(form: any): void {
    const dialogRef = this.dialog.open(DeleteFormDialogComponent, {
      height: '250px',
      width: '500px',
      data: { form: form }, // Pass the entire form object as data to the dialog
    });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
