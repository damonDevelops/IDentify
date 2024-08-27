import { Component, Inject } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiService } from 'src/app/_services/api/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { Subject, takeUntil } from 'rxjs';
@Component({
  selector: 'app-delete-form-dialog',
  templateUrl: './delete-form-dialog.component.html',
  styleUrls: ['./delete-form-dialog.component.scss'],
})
export class DeleteFormDialogComponent {
  formName: string = ''; // Initialize with an empty string
  private ngUnsubscribe = new Subject<void>();

  constructor(
    public dialogRef: MatDialogRef<DeleteFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private apiService: ApiService,
    private companyService: CompanyServiceService,
  ) {}

  onCloseClick(): void {
    this.dialogRef.close();
  }

  deleteForm(): void {
    // You can call your API service here to delete the form based on the formId.
    // After successful deletion, you can update the forms array or perform any other necessary actions.
    // Example:

    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService
            .deleteFile(this.data.form.id, activeCompany.id)
            .subscribe(
              () => {
                // Successful deletion, update the forms array or handle as needed.
                location.reload();
              },
              (error) => {
                console.error('Error deleting form:', error);
              },
            );
        }
      });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
