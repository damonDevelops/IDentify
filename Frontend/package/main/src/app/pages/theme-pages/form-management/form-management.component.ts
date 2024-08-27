import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CreateNewFormDialogComponent } from './create-new-form-dialog/create-new-form-dialog.component';
import { HowDoesThisWorkDialogComponent } from './how-does-this-work-dialog/how-does-this-work-dialog.component';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { Company } from 'src/app/_models/company';
import { ApiService } from 'src/app/_services/api/api.service';
import { Subject, takeUntil } from 'rxjs';
@Component({
  selector: 'app-form-management',
  templateUrl: './form-management.component.html',
  styleUrls: ['./form-management.component.scss'],
})
export class FormManagementComponent {
  activeCompany: Company;
  selectedFile: File | null = null;
  noFormsBool: boolean = false;

  private ngUnsubscribe = new Subject<void>();

  constructor(
    private dialog: MatDialog,
    private companyService: CompanyServiceService,
    private apiService: ApiService,
  ) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0] as File;
  }

  ngOnInit() {
    // Subscribe to the active company observable
    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.activeCompany = activeCompany;

          // Fetch forms for the active company
          this.apiService.getForms(activeCompany.id).subscribe(
            (forms) => {
              const numForms = forms.length;
              this.noFormsBool = numForms === 0; // Disable button if no forms
            },
            (error) => {
              console.error("Couldn't fetch forms: " + error);
            },
          );
        }
      });
  }

  createNewForm() {
    // Open the dialog
    const dialogRef = this.dialog.open(CreateNewFormDialogComponent, {
      width: '350px',
      data: {}, // You can pass data to your dialog component if needed
    });

    // Handle dialog close event (e.g., after the user interacts with the dialog)
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        // Handle the result from the dialog, if needed
        console.log('Dialog result:', result);
      }
    });
  }

  openHowDoesThisWorkDialog() {
    const dialogRef = this.dialog.open(HowDoesThisWorkDialogComponent, {
      maxWidth: '1000px',
    });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
