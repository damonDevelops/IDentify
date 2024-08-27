import { Component, Inject, NgZone, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiService } from 'src/app/_services/api/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { CdkTextareaAutosize } from '@angular/cdk/text-field';
import { Subject, take, takeUntil } from 'rxjs';

@Component({
  selector: 'app-create-new-form-dialog',
  templateUrl: './create-new-form-dialog.component.html',
  styleUrls: ['./create-new-form-dialog.component.scss'],
})
export class CreateNewFormDialogComponent {
  downloadAsPdf: boolean = true;
  formName: string = ''; // Initialize with an empty string
  description: string = ''; // Initialize with an empty string for description
  FormNameForm: FormGroup;
  selectedFile: File | null = null;

  @ViewChild('autosize') autosize: CdkTextareaAutosize;
  private ngUnsubscribe = new Subject<void>();

  constructor(
    public dialogRef: MatDialogRef<CreateNewFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private apiService: ApiService,
    private snackBar: MatSnackBar,
    private companyService: CompanyServiceService,
    private _ngZone: NgZone,
  ) {}

  onCloseClick(): void {
    this.dialogRef.close();
  }

  onSubmitClick(): void {
    // Pass the formName and selectedFile back to the parent component
    this.dialogRef.close({
      formName: this.formName,
      selectedFile: this.selectedFile,
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0] as File;
    if (file) {
      this.selectedFile = file;
    } else {
      // Handle the case when no file is selected
      this.selectedFile = null;
      // You can show a message to the user if needed
      console.error('No file selected.');
    }
  }

  // Function to determine the icon based on file type
  getIconForFileType(fileName: string): string {
    const fileExtension = fileName.split('.').pop()?.toLowerCase();

    if (fileExtension === 'pdf') {
      return '../../../../../assets/images/svgs/pdf-icon.svg'; // Replace with the path to your PDF icon
    } else if (fileExtension === 'docx') {
      return '../../../../../assets/images/svgs/word-doc-icon.svg'; // Replace with the path to your Word icon
    } else {
      return ''; // Replace with a default icon for unknown file types
    }
  }

  createForm() {
    if (this.selectedFile) {
      // Create a FormData object to send the file as multipart/form-data
      const formData = new FormData();
      formData.append('file', this.selectedFile); // 'file' should match the server's expected parameter name
      formData.append('fileName', this.formName); // Add formName
      formData.append('description', this.description); // Add description

      this.companyService
        .getActiveCompany()
        .pipe(takeUntil(this.ngUnsubscribe))
        .subscribe((activeCompany) => {
          if (activeCompany) {
            this.apiService.uploadForm(formData, activeCompany.id).subscribe(
              () => {
                console.log('Got here');
                window.location.reload();
              },
              (error) => {
                // Handle errors here
                console.error('Error:', error);
              },
            );
          }
        });
    } else {
      // Handle the case when no file is selected
      console.error('No file selected.');
    }
  }

  triggerResize() {
    // Wait for changes to be applied, then trigger textarea resize.
    this._ngZone.onStable
      .pipe(take(1))
      .subscribe(() => this.autosize.resizeToFitContent(true));
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
